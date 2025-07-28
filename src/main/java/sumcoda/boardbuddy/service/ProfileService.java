package sumcoda.boardbuddy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import sumcoda.boardbuddy.config.AwsS3Config;
import sumcoda.boardbuddy.dto.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.MemberProfileInfoDTO;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;
import sumcoda.boardbuddy.dto.fetch.ProfileImageObjectNameProjection;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.ProfileImage;
import sumcoda.boardbuddy.exception.member.InvalidFileFormatException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageRetrievalException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageSaveException;
import sumcoda.boardbuddy.repository.profileImage.ProfileImageRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;

import java.io.IOException;
import java.util.List;

import static sumcoda.boardbuddy.util.BadgeImageUtil.*;
import static sumcoda.boardbuddy.util.MemberProfileUtil.convertMemberProfileInfoDTO;
import static sumcoda.boardbuddy.util.ProfileImageUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final AwsS3Config awsS3Config;

    private final S3Client s3Client;

    private final ProfileImageRepository profileImageRepository;

    private final BadgeImageRepository badgeImageRepository;

    private final MemberRepository memberRepository;

    // 비밀번호를 암호화 하기 위한 필드
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CloudFrontSignedUrlService cloudFrontSignedUrlService;


    /**
     * 닉네임을 바탕으로 해당 유저의 프로필 조회
     *
     * @param nickname 유저 닉네임
     * @return 해당 닉네임의 유저 프로필
     **/
    public MemberProfileInfoDTO getMemberProfileByNickname(String nickname) {

        if (nickname == null) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        List<BadgeImageInfoDTO> badgeImageInfoDTOList = badgeImageRepository.findTop3BadgeImagesByNickname(nickname).stream()
                .map(projection -> {

                    String badgeImageS3RequestKey = buildBadgeImageS3RequestKey(projection.s3SavedObjectName());

                    String badgeImageSignedUrl = cloudFrontSignedUrlService.generateSignedUrl(badgeImageS3RequestKey);

                    return convertBadgeImageInfoDTO(projection, badgeImageSignedUrl);
                })
                .toList();

        MemberProfileProjection memberProfileProjection = memberRepository.findMemberProfileByNickname(nickname)
                .orElseThrow(() -> new MemberRetrievalException("프로필을 조회할 수 없습니다. 관리자에게 문의하세요."));

        String profileImageS3RequestKey = buildProfileImageS3RequestKey(memberProfileProjection.s3SavedObjectName());

        String profileImageSignedUrl = cloudFrontSignedUrlService.generateSignedUrl(profileImageS3RequestKey);

        return convertMemberProfileInfoDTO(memberProfileProjection, badgeImageInfoDTOList, profileImageSignedUrl);
    }

    /**
     * 사용자 이름을 바탕으로 해당 유저 프로필 수정
     *
     * @param username 유저 아이디
     * @param updateProfileDTO 수정할 정보가 담겨있는 DTO
     * @param profileImageFile 수정할 프로필 이미지 파일
     **/
    @Transactional
    public void updateProfile(String username, MemberRequest.UpdateProfileDTO updateProfileDTO, MultipartFile profileImageFile) {
        // 유저 아이디로 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 닉네임이 null이 아니면 업데이트
        if (updateProfileDTO.getNickname() != null) {
            member.assignNickname(updateProfileDTO.getNickname());
        }

        // 비밀번호가 null이 아니면 암호화 후 업데이트
        if (updateProfileDTO.getPassword() != null && !updateProfileDTO.getPassword().isEmpty()) {
            member.assignPassword(bCryptPasswordEncoder.encode(updateProfileDTO.getPassword()));
        }

        // 핸드폰 번호가 null이 아니면 업데이트
        if (updateProfileDTO.getPhoneNumber() != null) {
            member.assignPhoneNumber(updateProfileDTO.getPhoneNumber());
        }

        // 자기소개가 null이 아니면 업데이트
        if (updateProfileDTO.getDescription() != null) {
            member.assignDescription(updateProfileDTO.getDescription());
        }

        if (profileImageFile == null || profileImageFile.isEmpty()) {
            member.assignProfileImage(null);
        } else {
            // 이미지 파일 형식 검증
            String contentType = profileImageFile.getContentType();
            if (contentType != null && !contentType.startsWith("image")) {
                throw new InvalidFileFormatException("지원되지 않는 파일 형식입니다.");
            }
            try {

                String bucketName = awsS3Config.getBucketName();

                boolean isExistsProfileImage = profileImageRepository.existsByUsername(username);

                // 기존 프로필 이미지가 있다면 S3에서 삭제
                if (isExistsProfileImage) {
                    ProfileImageObjectNameProjection profileImageObjectNameProjection = profileImageRepository.findProfileImageObjectNameByUsername(username)
                            .orElseThrow(() -> new ProfileImageRetrievalException("서버 문제로 기존 프로필 이미지 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

                    String profileImageSavedObjectName = profileImageObjectNameProjection.s3SavedObjectName();

                    String s3DeleteRequestKey = buildProfileImageS3RequestKey(profileImageSavedObjectName);

                    // DeleteObjectRequest 생성
                    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3DeleteRequestKey)
                            .build();

                    s3Client.deleteObject(deleteObjectRequest);

                    member.assignProfileImage(null);

                    profileImageRepository.deleteByS3SavedObjectName(profileImageSavedObjectName);
                }

                // 원본 파일명에서 확장자만 추출
                String originalFilename = profileImageFile.getOriginalFilename();

                String s3SavedObjectName = buildS3SavedObjectName(originalFilename);

                String s3PutRequestKey = buildProfileImageS3RequestKey(s3SavedObjectName);

                // PutObjectRequest 생성
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3PutRequestKey)
                        .contentType(profileImageFile.getContentType())   // (Optional) Content-Type 설정
                        .build();

                // S3에 업로드
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(profileImageFile.getBytes()));


                // 새로운 프로필 이미지 생성
                ProfileImage newProfileImage = ProfileImage.buildProfileImage(
                        originalFilename,
                        s3SavedObjectName
                );

                member.assignProfileImage(newProfileImage);

                profileImageRepository.save(newProfileImage);


            } catch (IOException e) {
                throw new ProfileImageSaveException("프로필 이미지를 저장하는 동안 오류가 발생했습니다.");
            }
        }
    }


}

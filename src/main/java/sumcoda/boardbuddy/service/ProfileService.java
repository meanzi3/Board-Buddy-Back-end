package sumcoda.boardbuddy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import sumcoda.boardbuddy.config.AwsS3Config;
import sumcoda.boardbuddy.dto.client.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.client.MemberProfileInfoDTO;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.fetch.BadgeImageInfoProjection;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.ProfileImage;
import sumcoda.boardbuddy.exception.member.InvalidFileFormatException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageDeleteException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageSaveException;
import sumcoda.boardbuddy.mapper.BadgeImageMapper;
import sumcoda.boardbuddy.mapper.MemberProfileMapper;
import sumcoda.boardbuddy.repository.profileImage.ProfileImageRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    private final MemberProfileMapper memberProfileMapper;

    private final BadgeImageMapper badgeImageMapper;


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

        List<BadgeImageInfoProjection> badgeImageInfoProjections = badgeImageRepository.findTop3BadgeImagesByNickname(nickname);

        List<BadgeImageInfoDTO> badgeImageInfoDTOList = badgeImageMapper.toBadgeImageInfoDTOList(badgeImageInfoProjections);

        MemberProfileProjection memberProfileProjection = memberRepository.findMemberProfileByNickname(nickname)
                .orElseThrow(() -> new MemberRetrievalException("프로필을 조회할 수 없습니다. 관리자에게 문의하세요."));

        return memberProfileMapper.toMemberProfileInfoDTO(memberProfileProjection, badgeImageInfoDTOList);
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
        if (updateProfileDTO.getPassword() != null && !updateProfileDTO.getPassword().isBlank()) {
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

        // 프로필 이미지 업로드/교체 여부 확인
        final boolean isProfileImageNewUpload = (profileImageFile != null && !profileImageFile.isEmpty());

        // 업로드/교체 의도가 없으면 그대로 유지
        if (!isProfileImageNewUpload) {
            // 텍스트 필드만 갱신하고 종료
            return;
        }

        // 파일 검증
        final String contentType = profileImageFile.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileFormatException("지원되지 않는 파일 형식입니다.");
        }

        // 기존 이미지 엔티티 참조 (null일 수도 있음)
        final ProfileImage oldProfileImage = member.getProfileImage();

        final String bucketName = awsS3Config.getBucketName();

        final String originalFilename = Optional.ofNullable(profileImageFile.getOriginalFilename()).orElse("profile");

        final String s3SavedObjectName = buildS3SavedObjectName(originalFilename);

        final String putKey = buildProfileImageS3RequestKey(s3SavedObjectName);

        try {
            // 업로드 먼저 (실패 시 기존 이미지 보존)
            PutObjectRequest putObjectRequest = buildPutObjectRequest(profileImageFile, bucketName, putKey);

            RequestBody requestBody = convertRequestBody(profileImageFile);

            s3Client.putObject(putObjectRequest, requestBody);

        } catch (SdkException | IOException exception) {
            // 네트워크/자격/권한/타임아웃
            throw new ProfileImageSaveException("프로필 이미지를 저장하는 동안 오류가 발생했습니다.");
        }

        // 새 엔티티 생성 및 연관관계 교체 -> DB 저장
        ProfileImage newProfileImage = ProfileImage.buildProfileImage(
                originalFilename,
                s3SavedObjectName
        );

        member.assignProfileImage(newProfileImage);

        profileImageRepository.save(newProfileImage);

        // 업로드/교체 성공 후, 이전 이미지 정리
        if (oldProfileImage != null) {
            final String deleteKey = buildProfileImageS3RequestKey(oldProfileImage.getS3SavedObjectName());

            member.assignProfileImage(null);

            try {
                DeleteObjectRequest deleteObjectRequest = buildDeleteObjectRequest(bucketName, deleteKey);

                s3Client.deleteObject(deleteObjectRequest);
            } catch (SdkException exception) {
                // 네트워크/자격/권한/타임아웃
                throw new ProfileImageDeleteException("프로필 이미지를 저장하는 동안 오류가 발생했습니다.");
            }
        }
    }
}

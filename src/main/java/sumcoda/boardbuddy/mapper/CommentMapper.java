package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.CommentAuthorDTO;
import sumcoda.boardbuddy.dto.client.CommentInfoDTO;
import sumcoda.boardbuddy.dto.fetch.CommentInfoProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * CommentInfoProjection 리스트를 부모-자식 계층 구조의 DTO 리스트로 변환
     *
     * @param projections 댓글 프로젝션 리스트
     * @return 계층 구조가 반영된 CommentInfoDTO 리스트
     */
    public List<CommentInfoDTO> toCommentInfoDTOList(List<CommentInfoProjection> projections) {

        // parentId 기준으로 그룹핑
        Map<Long, List<CommentInfoProjection>> childrenMap = projections.stream()
                .filter(projection -> projection.parentId() != null)
                .collect(Collectors.groupingBy(CommentInfoProjection::parentId));

        // 최상위(parentId == null)만 뽑아서 재귀 매핑
        return projections.stream()
                .filter(projection -> projection.parentId() == null)
                .map(projection -> toCommentInfoDTO(projection, childrenMap))
                .collect(Collectors.toList());
    }

    /**
     * 단일 CommentInfoProjection을 DTO로 변환하며 재귀적으로 자식 댓글도 변환
     *
     * @param projection 변환할 프로젝션
     * @param childrenMap parentId → 자식 프로젝션 리스트 맵
     * @return 변환된 CommentInfoDTO
     */
    private CommentInfoDTO toCommentInfoDTO(CommentInfoProjection projection, Map<Long, List<CommentInfoProjection>> childrenMap) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        // AuthorDTO 생성
        CommentAuthorDTO author = CommentAuthorDTO.builder()
                .nickname(projection.nickname())
                .rank(projection.rank())
                .profileImageSignedURL(profileImageSignedURL)
                .build();

        // 자식 댓글 재귀 호출
        List<CommentInfoDTO> children = Optional.ofNullable(childrenMap.get(projection.id()))
                .orElseGet(List::of)
                .stream()
                .map(childProjection -> toCommentInfoDTO(childProjection, childrenMap))
                .collect(Collectors.toList());

        // 최종 DTO 빌드
        return CommentInfoDTO.builder()
                .id(projection.id())
                .author(author)
                .content(projection.content())
                .createdAt(projection.createdAt())
                .children(children)
                .build();
    }
}

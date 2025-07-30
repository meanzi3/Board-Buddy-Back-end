package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.ChatMessageItemInfoDTO;
import sumcoda.boardbuddy.dto.client.PageResponseDTO;
import sumcoda.boardbuddy.dto.fetch.ChatMessageItemInfoProjection;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.exception.ChatMessageRetrievalException;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static sumcoda.boardbuddy.util.ChatMessageUtil.ZONE;

@Component
@RequiredArgsConstructor
public class ChatMessageMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * 주어진 메시지 프로젝션 DTO를 채팅 메시지 항목 DTO로 변환
     *
     * @param projection 변환할 메시지 프로젝션 DTO
     * @return 변환된 ChatMessageItemInfoDTO 객체
     * @since 1.0
     * @version 2.0
     */
    public ChatMessageItemInfoDTO toChatMessageItemInfoDTO(ChatMessageItemInfoProjection projection) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        // 1) 공통 필드 세팅
        var builder = ChatMessageItemInfoDTO.builder()
                .id(projection.id())
                .content(projection.content())
                .messageType(projection.messageType())
                .sentAt(LocalDateTime.ofInstant(projection.sentAt(), ZONE));

        // 2) Talk 타입일 때만 추가 필드 세팅
        if (Objects.equals(projection.messageType(), MessageType.TALK)) {
            builder.nickname(projection.nickname())
                    .profileImageSignedURL(profileImageSignedURL)
                    .rank(projection.rank());
        }

        // 3) 빌드해서 반환
        return builder.build();
    }

    /**
     * 페이징 조회 결과를 클라이언트 제공용 DTO로 변환
     *
     * @param projections 레포지토리에서 내려온 PageResponseDTO
     * @param hasMore 조회 방향으로 메시지가 더 있는지 여부
     * @param nextCursor 조회 방향에 따른 첫번째 또는 마지막 메시지를 가리키는 커서
     * @return 클라이언트 제공용 ChatMessageItemInfoDTO 페이징 결과
     * @throws ChatMessageRetrievalException 데이터가 없거나 매핑에 실패할 때
     * @since 1.0
     * @version 2.0
     */
    public PageResponseDTO<ChatMessageItemInfoDTO> toChatMessagePageResponseDTO(
            List<ChatMessageItemInfoProjection> projections,
            Boolean hasMore,
            String nextCursor) {

        // 빈 페이지 예외 처리
        if (projections.isEmpty()) {
            throw new ChatMessageRetrievalException("서버 문제로 메시지를 조회하지 못하였습니다. 관리자에게 문의하세요");
        }

        // 내부 DTO → 최종 API DTO 매핑
        List<ChatMessageItemInfoDTO> dtoList = projections.stream()
                .map(this::toChatMessageItemInfoDTO)
                .collect(Collectors.toList());

        // 기존 page 의 hasMore, nextCursor 를 그대로 담아 새 PageResponseDTO 생성
        return PageResponseDTO.<ChatMessageItemInfoDTO>builder()
                .dataList(dtoList)
                .hasMore(hasMore)
                .nextCursor(nextCursor)
                .build();
    }

}

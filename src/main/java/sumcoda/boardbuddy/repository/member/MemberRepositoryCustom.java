package sumcoda.boardbuddy.repository.member;

import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;
import sumcoda.boardbuddy.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<AuthResponse.ProfileDTO> findAuthDTOByUsername(String username);

    Optional<MemberResponse.ProfileDTO> findMemberDTOByUsername(String username);

    List<MemberResponse.RankingsDTO> findTop3RankingMembers();

    List<Member> findAllOrderedByRankScore();

    Optional<MemberProfileProjection> findMemberProfileByNickname(String nickname);

    /**
     * @apiNote 현재는 사용률 저조로 메서드가 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    Optional<MemberResponse.LocationWithRadiusDTO> findLocationWithRadiusDTOByUsername(String username);

    Optional<MemberResponse.UsernameDTO> findUserNameDTOByUsername(String username);

    Optional<MemberResponse.IdDTO> findIdDTOByUsername(String username);

    Optional<MemberResponse.UsernameDTO> findUsernameDTOByNickname(String nickname);

    Optional<MemberResponse.NicknameDTO> findNicknameDTOByUsername(String username);

    /**
     * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
     */
//    List<String> findUsernamesWithGatherArticleInRange(String username, String sido, String sgg, String emd);


}

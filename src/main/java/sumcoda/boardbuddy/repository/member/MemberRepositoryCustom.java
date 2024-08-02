package sumcoda.boardbuddy.repository.member;

import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<AuthResponse.ProfileDTO> findAuthDTOByUsername(String username);

    Optional<MemberResponse.ProfileDTO> findMemberDTOByUsername(String username);

    List<MemberResponse.RankingsDTO> findTop3RankingMembers();

    List<Member> findAllOrderedByRankScore();

    Optional<MemberResponse.ProfileInfosDTO> findMemberProfileByNickname(String nickname);

    Optional<MemberResponse.LocationWithRadiusDTO> findLocationWithRadiusDTOByUsername(String username);

    Optional<MemberResponse.UserNameDTO> findUserNameDTOByUsername(String username);

    Optional<MemberResponse.IdDTO> findIdDTOByUsername(String username);

    Optional<MemberResponse.UserNameDTO> findUsernameDTOByNickname(String nickname);

    Optional<MemberResponse.NicknameDTO> findNicknameDTOByUsername(String username);
}

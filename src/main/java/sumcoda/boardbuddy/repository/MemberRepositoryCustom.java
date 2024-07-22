package sumcoda.boardbuddy.repository;

import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<AuthResponse.ProfileDTO> findAuthDTOByUsername(String username);

    Optional<MemberResponse.ProfileDTO> findMemberDTOByUsername(String username);

    List<MemberResponse.RankingsDTO> findTop3RankingMembers();

}

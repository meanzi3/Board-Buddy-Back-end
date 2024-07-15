package sumcoda.boardbuddy.repository;

import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<AuthResponse.ProfileDTO> findAuthDTOByUsername(String username);

    Optional<MemberResponse.ProfileDTO> findMemberDTOByUsername(String username);

}

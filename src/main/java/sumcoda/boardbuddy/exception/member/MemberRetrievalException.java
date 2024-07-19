package sumcoda.boardbuddy.exception.member;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MemberRetrievalException extends UsernameNotFoundException {
    public MemberRetrievalException(String message) {
        super(message);
    }
}

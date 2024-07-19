package sumcoda.boardbuddy.exception.member;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MemberNotFoundException extends UsernameNotFoundException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}

package sumcoda.boardbuddy.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.auth.oauth2.CustomOAuth2User;

@RestControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute("username")
  public String populateUsername(Authentication authentication) {
    String username = "";

    if (authentication != null) {
      if (authentication instanceof OAuth2AuthenticationToken) {
        // OAuth2.0 사용자
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        username = oauthUser.getUsername();
      } else {
        // 그외 사용자
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        username = userDetails.getUsername();
      }
    }

    return username;
  }
}

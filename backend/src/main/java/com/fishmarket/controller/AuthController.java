package com.fishmarket.controller;

import com.fishmarket.model.UserAccount;
import com.fishmarket.repository.UserAccountRepository;
import com.fishmarket.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserAccountRepository userAccountRepository;
  private final JwtService jwtService;
  private final GoogleIdTokenVerifier verifier;

  public AuthController(
      UserAccountRepository userAccountRepository,
      JwtService jwtService,
      @Value("${app.google.client-id}") String clientId) {
    this.userAccountRepository = userAccountRepository;
    this.jwtService = jwtService;
    this.verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(clientId))
            .build();
  }

  @PostMapping("/google")
  public AuthResponse loginWithGoogle(@RequestBody TokenRequest request)
      throws GeneralSecurityException, IOException {
    GoogleIdToken idToken = verifier.verify(request.token());
    if (idToken == null) {
      throw new UnauthorizedException();
    }

    GoogleIdToken.Payload payload = idToken.getPayload();
    String googleId = payload.getSubject();
    String name = (String) payload.get("name");
    String email = payload.getEmail();
    String picture = (String) payload.get("picture");

    UserAccount account =
        userAccountRepository.findByGoogleId(googleId).orElseGet(() -> new UserAccount(googleId, name, email, picture));
    account.setName(name);
    account.setEmail(email);
    account.setAvatarUrl(picture);
    userAccountRepository.save(account);

    String token = jwtService.issueToken(googleId);
    return new AuthResponse(token, account.getName(), account.getEmail(), account.getAvatarUrl());
  }

  @GetMapping("/me")
  public UserProfile me(Authentication authentication) {
    String googleId = Optional.ofNullable(authentication).map(Authentication::getName).orElse(null);
    if (googleId == null) {
      throw new UnauthorizedException();
    }
    UserAccount account =
        userAccountRepository.findByGoogleId(googleId).orElseThrow(UnauthorizedException::new);
    return new UserProfile(account.getName(), account.getEmail(), account.getAvatarUrl());
  }

  public record TokenRequest(String token) {}

  public record AuthResponse(String token, String name, String email, String avatarUrl) {}

  public record UserProfile(String name, String email, String avatarUrl) {}

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  private static class UnauthorizedException extends RuntimeException {}
}

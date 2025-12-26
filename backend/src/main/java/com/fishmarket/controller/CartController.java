package com.fishmarket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishmarket.model.UserAccount;
import com.fishmarket.repository.UserAccountRepository;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
  private final UserAccountRepository userAccountRepository;
  private final ObjectMapper objectMapper;

  public CartController(UserAccountRepository userAccountRepository, ObjectMapper objectMapper) {
    this.userAccountRepository = userAccountRepository;
    this.objectMapper = objectMapper;
  }

  @GetMapping
  public CartResponse getCart(Authentication authentication) {
    UserAccount account = resolveAccount(authentication);
    String cartJson = account.getCartJson();
    return new CartResponse(cartJson == null ? "[]" : cartJson);
  }

  @PutMapping
  public CartResponse saveCart(@RequestBody Object cartPayload, Authentication authentication)
      throws JsonProcessingException {
    UserAccount account = resolveAccount(authentication);
    String cartJson = objectMapper.writeValueAsString(cartPayload);
    account.setCartJson(cartJson == null || cartJson.isBlank() ? "[]" : cartJson);
    userAccountRepository.save(account);
    return new CartResponse(account.getCartJson());
  }

  private UserAccount resolveAccount(Authentication authentication) {
    String googleId = Optional.ofNullable(authentication).map(Authentication::getName).orElse(null);
    if (googleId == null) {
      throw new UnauthorizedException();
    }
    return userAccountRepository.findByGoogleId(googleId).orElseThrow(UnauthorizedException::new);
  }

  public record CartResponse(String cartJson) {}

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  private static class UnauthorizedException extends RuntimeException {}
}

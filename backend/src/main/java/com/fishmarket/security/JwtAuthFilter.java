package com.fishmarket.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // ---- DEBUG LOGS (added) ----
    System.out.println("JwtAuthFilter HIT -> " + request.getMethod() + " " + request.getRequestURI());

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null) {
      System.out.println("Authorization header: null");
    } else {
      String preview = header.substring(0, Math.min(25, header.length()));
      System.out.println("Authorization header preview: " + preview + (header.length() > 25 ? "..." : ""));
    }
    // ----------------------------

    if (header == null || !header.startsWith("Bearer ")) {
      System.out.println("No Bearer token, continuing as anonymous");
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring("Bearer ".length());

    try {
      Claims claims = jwtService.parseClaims(token);
      String subject = claims.getSubject();

      // ---- DEBUG LOGS (added) ----
      System.out.println("JWT parsed OK. subject=" + subject);
      // ----------------------------

      if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            subject, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ---- DEBUG LOGS (added) ----
        System.out.println("SecurityContext set for subject=" + subject);
        // ----------------------------
      } else {
        // ---- DEBUG LOGS (added) ----
        System.out.println("Subject was null OR authentication already existed in SecurityContext");
        // ----------------------------
      }
    } catch (Exception e) {
      System.out.println("JWT rejected: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      // proceed without auth (for now)
    }

    filterChain.doFilter(request, response);
  }
}

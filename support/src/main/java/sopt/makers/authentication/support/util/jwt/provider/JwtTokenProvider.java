package sopt.makers.authentication.support.util.jwt.provider;

import static sopt.makers.authentication.support.util.code.failure.TokenFailure.TOKEN_EXPIRED;
import static sopt.makers.authentication.support.util.code.failure.TokenFailure.UNSUPPORTED_ISSUER;

import sopt.makers.authentication.support.system.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.util.code.failure.TokenFailure;
import sopt.makers.authentication.support.util.exception.TokenException;
import sopt.makers.authentication.support.util.jwt.JwtProvider;
import sopt.makers.authentication.support.value.JwtConstant;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements JwtProvider<CustomAuthentication> {

  private final JwtDecoder jwtDecoder;
  private final JwtEncoder jwtEncoder;

  @Override
  public String generateAccessToken(final CustomAuthentication value) {
    String subject = value.getPrincipal().toString();
    String issuer = JwtConstant.ISSUER;
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(JwtConstant.ACCESS_TOKEN_EXPIRATION);
    List<String> roles =
        value.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableList());
    JwtClaimsSet claimsSet = generateClaimSet(subject, issuer, now, expiration);
    return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }

  @Override
  public String generateRefreshToken(CustomAuthentication value) {
    String subject = value.getPrincipal().toString();
    String issuer = JwtConstant.ISSUER;
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(JwtConstant.REFRESH_TOKEN_EXPIRATION);
    List<String> roles =
        value.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableList());
    JwtClaimsSet claimsSet = generateClaimSet(subject, issuer, now, expiration);
    return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
  }

  @Override
  public CustomAuthentication parse(final String token) throws IOException {
    validatePrefix(token);
    String seperatedToken = separatePrefix(token);
    Jwt jwt = jwtDecoder.decode(seperatedToken);
    validateJwt(jwt);
    return resolveAuthentication(jwt);
  }

  private String separatePrefix(String token) {
    return token.substring(JwtConstant.TOKEN_HEADER.length());
  }

  private void validatePrefix(String token) {
    if (!token.startsWith(JwtConstant.TOKEN_HEADER)) {
      throw new TokenException(TokenFailure.INVALID_PREFIX);
    }
  }

  public void validateJwt(final Jwt jwt) {
    validateExpiration(jwt);
    validateIssuer(jwt);
    validateSubject(jwt);
  }

  private JwtClaimsSet generateClaimSet(
      String subject, String issuer, Instant issuedAt, Instant expiresAt) {
    return JwtClaimsSet.builder()
        .subject(subject)
        .issuer(issuer)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .build();
  }

  private void validateExpiration(final Jwt jwt) {
    Date expiration = Date.from(jwt.getExpiresAt());
    if (expiration == null || expiration.before(new Date())) {
      throw new TokenException(TOKEN_EXPIRED);
    }
  }

  private void validateIssuer(final Jwt jwt) {
    String issuer = jwt.getClaim(JwtClaimNames.ISS);

    Optional.ofNullable(issuer)
        .filter(i -> Arrays.asList(JwtConstant.ISSUERS).contains(i))
        .orElseThrow(() -> new TokenException(UNSUPPORTED_ISSUER));
  }

  private void validateSubject(final Jwt jwt) {
    // DB연결 필요 ? (논의)
  }

  private CustomAuthentication resolveAuthentication(final Jwt jwt) {
    String subject = jwt.getSubject();
    Long userId = Long.parseLong(subject);
    List<GrantedAuthority> authorities = extractAuthorities(jwt);
    return new CustomAuthentication(subject, authorities);
  }

  private List<GrantedAuthority> extractAuthorities(final Jwt jwt) {
    List<String> roles = jwt.getClaim("roles");
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
  }

  private String appendTokenHeader(final String IncompleteToken) {
    return JwtConstant.TOKEN_HEADER.concat(IncompleteToken);
  }
}

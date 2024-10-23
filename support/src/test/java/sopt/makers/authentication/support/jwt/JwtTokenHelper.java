package sopt.makers.authentication.support.jwt;

import sopt.makers.authentication.support.system.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.value.JwtConstant;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public class JwtTokenHelper {

  public static Jwt generateJwt() {

    try {
      RSAKey rsaKey = new RSAKeyGenerator(2048).generate();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }

    Jwt jwt =
        Jwt.withTokenValue("test")
            .expiresAt(new Date(new Date().getTime() + 60 * 1000).toInstant()) // 1분 후 만료
            .claim("userId", "1")
            .subject("testToken")
            .header("TestHeader", "headerValue")
            .build();
    return jwt;
  }

  public static CustomAuthentication createFakeAuthentication(String userId) {
    return new CustomAuthentication(userId, "password");
  }

  public static Jwt createFakeAccessTokenJwt(JwtClaimsSet claimsSet) {
    String tokenValue = "mockAccessToken";
    String subject = claimsSet.getSubject();
    String issuer = JwtConstant.ISSUER;
    Instant issuedAt = claimsSet.getIssuedAt();
    Instant expiresAt = claimsSet.getExpiresAt();
    Object roles = claimsSet.getClaim("roles");

    // Jwt 객체 생성
    Jwt jwt =
        Jwt.withTokenValue(tokenValue)
            .header("TestHeader", "headerValue") // 헤더 설정
            .subject(subject)
            .issuer(issuer)
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .claim("roles", roles)
            .build();

    return jwt;
  }

  public static Jwt createFakeRefreshTokenJwt(JwtClaimsSet claimsSet) {
    String tokenValue = "mockRefreshToken"; // 실제 JWT 토큰 값
    String subject = claimsSet.getSubject(); // subject 추출
    String issuer = JwtConstant.ISSUER; // issuer 추출
    Instant issuedAt = claimsSet.getIssuedAt(); // issuedAt 추출
    Instant expiresAt = claimsSet.getExpiresAt(); // expiresAt 추출
    Object roles = claimsSet.getClaim("roles"); // roles 클레임 추출

    // Jwt 객체 생성
    Jwt jwt =
        Jwt.withTokenValue(tokenValue)
            .header("TestHeader", "headerValue") // 헤더 설정
            .subject(subject)
            .issuer(issuer)
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .claim("roles", roles)
            .build();

    return jwt;
  }
}

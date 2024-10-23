package sopt.makers.authentication.support.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import sopt.makers.authentication.support.system.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.util.exception.TokenException;
import sopt.makers.authentication.support.util.jwt.provider.JwtTokenProvider;
import sopt.makers.authentication.support.value.JwtConstant;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

  @Mock private JwtDecoder decoder;

  @Mock private JwtEncoder encoder;

  @InjectMocks private JwtTokenProvider jwtTokenProvider;

  @Nested
  @DisplayName("토큰 생성 테스트")
  class GenerateTokenTest {

    @Test
    @DisplayName("ATK 토큰 생성 테스트")
    public void generate1() {
      // Given
      String userId = "1";
      CustomAuthentication fakeAuthentication = JwtTokenHelper.createFakeAuthentication(userId);

      Instant now = Instant.now();
      Instant expiration = now.plusSeconds(JwtConstant.ACCESS_TOKEN_EXPIRATION);
      JwtClaimsSet claimsSet =
          JwtClaimsSet.builder()
              .subject(userId)
              .issuer(JwtConstant.ISSUER)
              .issuedAt(now)
              .expiresAt(expiration)
              .claim("roles", List.of("ROLE_USER"))
              .build();

      Jwt givenJwt = JwtTokenHelper.createFakeAccessTokenJwt(claimsSet);
      JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(claimsSet);

      // When
      when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(givenJwt);
      String expectedAccessToken = jwtTokenProvider.generateAccessToken(fakeAuthentication);

      // Then
      assertNotNull(expectedAccessToken);
      assertThat(expectedAccessToken).isEqualTo("mockAccessToken");
    }

    @Test
    @DisplayName("RTK 토큰 생성 테스트")
    public void test2() {
      // Given
      String userId = "1";
      CustomAuthentication fakeAuthentication = JwtTokenHelper.createFakeAuthentication(userId);

      Instant now = Instant.now();
      Instant expiration = now.plusSeconds(JwtConstant.REFRESH_TOKEN_EXPIRATION);
      JwtClaimsSet claimsSet =
          JwtClaimsSet.builder()
              .subject(userId)
              .issuer(JwtConstant.ISSUER)
              .issuedAt(now)
              .expiresAt(expiration)
              .claim("roles", List.of("ROLE_USER"))
              .build();

      Jwt givenJwt = JwtTokenHelper.createFakeRefreshTokenJwt(claimsSet);
      JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(claimsSet);

      // When
      when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(givenJwt);
      String expectedRefreshToken = jwtTokenProvider.generateRefreshToken(fakeAuthentication);

      // Then
      assertNotNull(expectedRefreshToken);
      assertThat(expectedRefreshToken).isEqualTo("mockRefreshToken");
    }
  }

  @Nested
  @DisplayName("토큰 검증 테스트")
  public class ValidateTokenTest {

    @Test
    @DisplayName("토큰은 Bearer로 시작해야 한다.")
    public void test1() {
      // Given
      String userId = "1";
      String token = "Bearer mockAccessToken";
      String invalidToken = "mockAccessToken";
      CustomAuthentication fakeAuthentication = JwtTokenHelper.createFakeAuthentication(userId);

      Instant now = Instant.now();
      Instant expiration = now.plusSeconds(JwtConstant.REFRESH_TOKEN_EXPIRATION);

      JwtClaimsSet claimsSet =
          JwtClaimsSet.builder()
              .subject(userId)
              .issuer(JwtConstant.ISSUER)
              .issuedAt(now)
              .expiresAt(expiration)
              .claim("roles", List.of("ROLE_USER"))
              .build();

      Jwt jwt = JwtTokenHelper.createFakeAccessTokenJwt(claimsSet);

      // When
      // Then
      jwtTokenProvider.validateJwt(jwt);
      assertThat(token).startsWith("Bearer");
      assertThatThrownBy(() -> jwtTokenProvider.parse(invalidToken))
          .isInstanceOf(TokenException.class);
    }

    //    @Test
    //    @DisplayName("생성된 토큰은 유효해야 한다.")

    //    @Test
    //    @DisplayName("토큰을 파싱할 수 있어야 한다.")

    //  @Test
    //  @DisplayName("파싱된 토큰 안의 Claims에는 User 정보가 담겨 있어야 한다.")
  }
}

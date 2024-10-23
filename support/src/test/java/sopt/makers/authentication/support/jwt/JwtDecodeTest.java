package sopt.makers.authentication.support.jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.nimbusds.jwt.SignedJWT;

@ExtendWith(MockitoExtension.class)
public class JwtDecodeTest {

  @Mock private JwtDecoder jwtDecoder;

  @Test
  @DisplayName("JWT 디코딩 호출을 확인한다")
  public void test() {
    // Given
    String testToken = "testToken";
    SignedJWT signedJWT = mock(SignedJWT.class);
    Jwt jwt = JwtTokenHelper.generateJwt();

    // When
    when(jwtDecoder.decode(testToken)).thenReturn(jwt);
    Jwt decoderJwt = jwtDecoder.decode(testToken);

    // Then
    verify(jwtDecoder, times(1)).decode(testToken);
  }
}

package sopt.makers.authentication.infra.client.openfeign.config;

import sopt.makers.authentication.infra.value.ClientConstant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PlaygroundClientConfig {

  private final ClientProperty clientValue;

  @Bean
  public RequestInterceptor playgroundRequestInterceptor() {
    return new BearerAuthRequestInterceptor(
        ClientConstant.AUTHORIZATION_TOKEN_PREFIX + clientValue.playground().token()) {
      @Override
      public void apply(final RequestTemplate template) {
        template.header(HttpHeaders.AUTHORIZATION, getToken());
      }
    };
  }
}

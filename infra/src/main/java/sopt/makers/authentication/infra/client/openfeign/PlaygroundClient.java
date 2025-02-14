package sopt.makers.authentication.infra.client.openfeign;

import sopt.makers.authentication.infra.client.openfeign.config.OpenFeignConfig;
import sopt.makers.authentication.infra.client.openfeign.config.PlaygroundClientConfig;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "playgroundClient",
    url = "${external.sopt-makers.playground.url}",
    configuration = {OpenFeignConfig.class, PlaygroundClientConfig.class})
public interface PlaygroundClient {}

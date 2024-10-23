package sopt.makers.authentication.support.util.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa")
public record JwtKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {}

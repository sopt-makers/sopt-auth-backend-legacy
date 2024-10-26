package sopt.makers.authentication.support.system.code.failure;

import sopt.makers.authentication.support.common.code.Failure;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthFailure implements Failure {

// 401

// 403

;
  private final HttpStatus status;
  private final String message;
}

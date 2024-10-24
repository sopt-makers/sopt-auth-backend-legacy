package sopt.makers.authentication.support.common.exception;

import sopt.makers.authentication.support.common.code.Failure;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final Failure failure;

  public BaseException(final Failure failure) {
    super(failure.getMessage());
    this.failure = failure;
  }
}

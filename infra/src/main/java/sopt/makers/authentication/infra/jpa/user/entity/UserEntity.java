package sopt.makers.authentication.infra.jpa.user.entity;

import sopt.makers.authentication.infra.jpa.common.*;
import sopt.makers.authentication.user.*;

import java.time.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class UserEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull String name;
  @NotNull String phone;
  @NotNull String email;
  @NotNull LocalDate birthday;
  @NotNull String authPlatformId;
  @NotNull AuthPlatform authPlatformType;
  @NotNull Boolean isActive;

  public UserEntity(final User user, boolean isActive) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();

    this.name = profile.name();
    this.phone = profile.phone();
    this.email = profile.email();
    this.birthday = profile.birthday();
    this.authPlatformId = socialAccount.authPlatformId();
    this.authPlatformType = socialAccount.authPlatformType();
    this.isActive = isActive;
  }
}

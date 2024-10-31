package sopt.makers.authentication.infra.jpa.user.entity;

import sopt.makers.authentication.user.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_ACTIVITY_HISTORIES")
public class UserActivityHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotNull
  private UserEntity user;

  @Min(1)
  private int generation;

  private Team team;
  @NotNull private Part part;
  @NotNull private Role role;

  public UserActivityHistoryEntity(final UserEntity user, final Activity activity) {
    this.user = user;
    this.generation = activity.generation();
    this.team = activity.team();
    this.part = activity.part();
    this.role = activity.role();
  }

  public Activity toDomain() {
    return new Activity(generation, team, part);
  }
}

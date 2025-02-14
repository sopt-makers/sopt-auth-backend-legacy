package sopt.makers.authentication.user;

import java.util.ArrayList;
import java.util.List;

public class ActivityList {

  private final List<Activity> activities;

  public ActivityList() {
    this.activities = new ArrayList<>();
  }

  public ActivityList(final List<Activity> activityList) {
    this.activities = activityList;
  }

  public ActivityList addActivity(final Activity activity) {
    validateActivityDuplication(activity);
    validateActivityEmpty(activity);
    List<Activity> updatedActivities = new ArrayList<>(this.activities);
    updatedActivities.add(activity);
    return new ActivityList(updatedActivities);
  }

  public Activity getFirstActivity() {
    return activities.getFirst();
  }

  public Activity getLastActivity() {
    return activities.getLast();
  }

  public int getTotalActivitySize() {
    return activities.size();
  }

  private void validateActivityDuplication(final Activity activity) {
    activities.stream()
        .filter(a -> a.equals(activity))
        .findAny()
        .ifPresent(
            a -> {
              throw new IllegalArgumentException("이미 존재하는 활동 정보입니다.");
            });
  }

  private void validateActivityEmpty(final Activity activity) {
    if (activity == null) {
      throw new IllegalArgumentException("활동이 비어있습니다.");
    }
    activity.validateActivityContentsEmpty();
  }
}

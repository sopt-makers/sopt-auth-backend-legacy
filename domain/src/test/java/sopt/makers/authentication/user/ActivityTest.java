package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ActivityTest {

	@Test
	public void 활동_이력은_NOTNULL() {
		// given
		ActivityList activityList = new ActivityList();
		Activity activity = null;

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 같은_활동_이력은_동시에_존재할_수_없다() {
		// given
		ActivityList activityList = new ActivityList();
		Activity activity1 = ActivityTestUtil.createActivity();

		activityList.addActivity(activity1);

		// when
		Part part = Part.ANDROID;
		Activity activity2 = ActivityTestUtil.createActivity();

		// then
		assertThatThrownBy(() -> activityList.addActivity(activity2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 활동은_TEAM_ENUM을_가진다() {
		// given
		Team team = Team.MAKERS;
		Activity activity = new Activity(34, team, Part.IOS, Role.MEMBER);

		// when
		Team team1 = activity.team;

		// then
		assertThat(team1).isEqualTo(Team.MAKERS);
	}

	@Test
	public void Team은_NULL이_가능하다() {
		// given
		Team team = null;
		Role role = Role.MEMBER;

		Activity activity = new Activity(34, team, Part.IOS, role);
		ActivityList activityList = new ActivityList();

		// when
		Team team1 = activity.team;
		activityList.addActivity(activity);

		// then
		assertThat(team1).isEqualTo(null);
		assertThat(activityList.getFirstActivity().team).isEqualTo(null);
	}

	@Test
	public void 활동_이력은_활동_파트를_가진다() {
		// given
		Part part = Part.ANDROID;
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, Role.MEMBER);

		// when
		Part part1 = activity.part;

		// then
		assertThat(part1).isEqualTo(Part.ANDROID);
	}

	@Test
	public void 활동_이력은_NOT_NULL() {
		// given
		ActivityList activityList = new ActivityList();
		Part part = Part.ANDROID;
		Activity activity = new Activity(34, Team.MAKERS, null, Role.MEMBER);

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void 활동은_역할을_가진다() {
		// given
		Role role = Role.MEMBER;
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, role);

		// when
		Role role1 = activity.role;

		// then
		assertThat(role1).isEqualTo(Role.MEMBER);
	}

	@Test
	public void 역할은_NOT_NULL() {
		// given
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, null);
		ActivityList activityList = new ActivityList();

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}
}

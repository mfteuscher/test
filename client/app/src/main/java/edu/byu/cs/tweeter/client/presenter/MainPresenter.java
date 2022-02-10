package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.iIsFollowerObserver;
import edu.byu.cs.tweeter.client.presenter.view.View;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    private final FollowerService followerService;
    private final StatusService statusService;

    public MainPresenter(Activity activity) {
        super(activity);
        followerService = new FollowerService();
        statusService = new StatusService();
    }

    public interface Activity extends View {
        void updateFollowersCount(int count);
        void updateFollowingCount(int count);
        void updateFollowButton(boolean removed);
        void setFollowerButton(boolean isFollower);
        void logoutUser();
    }

    public void isFollower(User selectedUser) {
        followerService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void followUser(User selectedUser) {
        view.displayMessage("Adding " + selectedUser.getName() + "...");
        followerService.FollowUser(selectedUser, new FollowObserver());
        updateSelectedUserFollowingAndFollowers(selectedUser);
    }

    public void unfollowUser(User selectedUser) {
        view.displayMessage("Removing " + selectedUser.getName() + "...");
        followerService.UnfollowUser(selectedUser, new UnfollowObserver());
        updateSelectedUserFollowingAndFollowers(selectedUser);
    }

    public void updateSelectedUserFollowingAndFollowers(User user) {
        followerService.GetFollowingAndFollowerCount(user, new GetFollowingCountObserver(), new GetFollowerCountObserver());
    }

    public void postStatus(String post) {
        statusService.PostStatus(post, new PostStatusObserver());
    }

    public boolean compareUsers(User selectedUser) {
        return userService.compareUsers(selectedUser);
    }

    public void logOut() {
        userService.logout(new LogOutObserver());
    }

    public class IsFollowerObserver extends Observer implements iIsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            getActivity().setFollowerButton(isFollower);
        }

        @Override
        protected String getActionType() {
            return "determine following relationship";
        }
    }

    public class GetFollowerCountObserver extends Observer implements GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            getActivity().updateFollowersCount(count);
        }

        @Override
        protected String getActionType() {
            return "get followers count";
        }
    }

    public class GetFollowingCountObserver extends Observer implements GetCountObserver {

        @Override
        public void handleSuccess(int count) {
            getActivity().updateFollowingCount(count);
        }

        @Override
        protected String getActionType() {
            return "get following count";
        }
    }

    public class FollowObserver extends Observer implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            getActivity().updateFollowButton(false);
        }

        @Override
        protected String getActionType() {
            return "follow";
        }
    }

    public class UnfollowObserver extends Observer implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            getActivity().updateFollowButton(true);
        }

        @Override
        protected String getActionType() {
            return "unfollow";
        }
    }

    public class PostStatusObserver extends Observer implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            getActivity().displayMessage("Successfully Posted!");
        }

        @Override
        protected String getActionType() {
            return "post status";
        }

    }

    public class LogOutObserver extends Observer implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            getActivity().logoutUser();
        }

        @Override
        protected String getActionType() {
            return "logout";
        }
    }

    private Activity getActivity() {
        return (Activity) view;
    }

}

package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    private final Activity activity;
    private final UserService userService;
    private final FollowerService followerService;
    private final StatusService statusService;

    public MainPresenter(Activity activity) {
        this.activity = activity;
        userService = new UserService();
        followerService = new FollowerService();
        statusService = new StatusService();
    }

    public interface Activity {
        void updateFollowersCount(int count);
        void updateFollowingCount(int count);
        void updateFollowButton(boolean removed);
        void setFollowerButton(boolean isFollower);
        void logoutUser();
        void displayMessage(String message);
    }

    public void isFollower(User selectedUser) {
        followerService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void followUser(User selectedUser) {
        activity.displayMessage("Adding " + selectedUser.getName() + "...");
        followerService.FollowUser(selectedUser, new FollowObserver());
        updateSelectedUserFollowingAndFollowers(selectedUser);
    }

    public void unfollowUser(User selectedUser) {
        activity.displayMessage("Removing " + selectedUser.getName() + "...");
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

    public class IsFollowerObserver implements FollowerService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            activity.setFollowerButton(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to determine following relationship because of exception: " + e.getMessage());
        }
    }

    public class GetFollowerCountObserver implements FollowerService.GetFollowerCountObserver {

        @Override
        public void handleSuccess(int count) {
            activity.updateFollowersCount(count);
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to get followers count because of exception: " + e.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowerService.GetFollowingCountObserver {

        @Override
        public void handleSuccess(int count) {
            activity.updateFollowingCount(count);
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to get following count because of exception: " + e.getMessage());
        }
    }

    public class FollowObserver implements FollowerService.FollowObserver {

        @Override
        public void handleSuccess() {
            activity.updateFollowButton(false);
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to follow because of exception: " + e.getMessage());
        }
    }

    public class UnfollowObserver implements FollowerService.UnfollowObserver {

        @Override
        public void handleSuccess() {
            activity.updateFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to unfollow: " + message);
            activity.updateFollowButton(false);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to unfollow because of exception: " + e.getMessage());
            activity.updateFollowButton(false);
        }
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            activity.displayMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleExceptions(Exception e) {
            activity.displayMessage("Failed to post status because of exception: " + e.getMessage());
        }
    }

    public class LogOutObserver implements UserService.LogOutObserver {

        @Override
        public void handleSuccess() {
            activity.logoutUser();
        }

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to logout because of exception: " + e.getMessage());
        }
    }

}

package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private UserService userService;
    private FollowerService followerService;
    private Activity activity;

    public MainPresenter(Activity activity) {
        userService = new UserService();
        followerService = new FollowerService();
        this.activity = activity;
    }

    public void updateSelectedUserFollowingAndFollowers(User user) {
        followerService.getFollowingAndFollowerCount(user, new GetFollowingCountObserver(), new GetFollowerCountObserver());
    }

    public void unfollowUser(User selectedUser) {
        activity.displayMessage("Removing " + selectedUser.getName() + "...");
        followerService.unfollowUser(selectedUser, new UnfollowObserver());
        updateSelectedUserFollowingAndFollowers(selectedUser);
    }

    public interface Activity {
        void displayMessage(String message);
        void updateFollowingCount(int count);
        void updateFollowersCount(int count);
        void updateFollowButton(boolean removed);
    }

    public class LogOutObserver implements UserService.LogOutObserver {

        @Override
        public void handleFailure(String message) {
            activity.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception e) {
            activity.displayMessage("Failed to logout because of exception: " + e.getMessage());
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



    public void logOut() {
        userService.logout(new LogOutObserver());
    }

}

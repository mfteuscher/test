package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter {

    private static final int PAGE_SIZE = 10;

    private View view;
    private UserService userService;
    private FollowerService followerService;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public FollowerPresenter(View view) {
        userService = new UserService();
        followerService = new FollowerService();
        this.view = view;
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }

    public void getFollowers(User user) {
        followerService.getFollowers(user, lastFollower, PAGE_SIZE, new GetFollowersObserver());
    }

    public interface View {
        void displayMessage(String message);
        void showFollowersList(User user);
        void addFollowers(List<User> followers);
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.showFollowersList(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayMessage("Failed to get user's profile because of exception: " + e.getMessage());
        }
    }

    public class GetFollowersObserver implements FollowerService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean morePages, User user) {
            isLoading = false;
            hasMorePages = morePages;
            lastFollower = user;
            view.addFollowers(followers);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayMessage("Failed to get followers because of exception: " + e.getMessage());
        }
    }

}

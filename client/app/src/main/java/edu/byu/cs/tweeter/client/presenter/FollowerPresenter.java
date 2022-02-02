package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter {

    private static final int PAGE_SIZE = 10;

    private final View view;
    private final UserService userService;
    private final FollowerService followerService;

    private User lastFollower;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public FollowerPresenter(View view) {
        this.view = view;
        userService = new UserService();
        followerService = new FollowerService();
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }

    public void getFollowers(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followerService.GetFollowers(user, lastFollower, PAGE_SIZE, new GetFollowersObserver());
        }
    }

    public interface View {
        void addFollowers(List<User> followers);
        void openUserActivity(User user);
        void displayMessage(String message);
        void setLoadingFooter(boolean loading);
    }

    public class GetFollowersObserver implements FollowerService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean morePages, User user) {
            isLoading = false;
            view.setLoadingFooter(false);
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

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.openUserActivity(user);
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

}

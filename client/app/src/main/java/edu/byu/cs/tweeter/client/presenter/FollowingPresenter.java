package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {

    private static final int PAGE_SIZE = 10;

    private View view;
    private FollowerService followerService;
    private UserService userService;

    private User lastFollowee;
    private boolean hasMorePages;

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

    private boolean isLoading = false;


    public FollowingPresenter(View view) {
        this.view = view;
        followerService = new FollowerService();
        userService = new UserService();
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }


    public interface View {
        void displayErrorMessage(String message);
        void setLoadingStatus(boolean value);
        void addFollowees(List<User> followees);
        void showFollowingList(User user);
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followerService.getFollowing(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver implements FollowerService.GetFollowingObserver {

        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowees(followees);
            setHasMorePages(hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception e) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get following because of exception: " + e.getMessage());
        }
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.showFollowingList(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + e.getMessage());
        }
    }

}

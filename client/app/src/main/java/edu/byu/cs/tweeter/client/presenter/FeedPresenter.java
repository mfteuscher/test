package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    private View view;
    private UserService userService;
    private StatusService statusService;

    private Status lastStatus;
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

    public FeedPresenter(View view) {
        userService = new UserService();
        statusService = new StatusService();
        this.view = view;
    }

    public void getUser(String username) {
            userService.getUser(username, new GetUserObserver());
    }

    public void loadMoreItems(User user) {
            isLoading = true;
            statusService.getFeed(user, lastStatus, new GetFeedObserver());
    }

    public interface View {
        void displayMessage(String message);
        void openUserFeed(User user);
        void addItems(List<Status> newFeed);
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.openUserFeed(user);
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

    public class GetFeedObserver implements StatusService.GetFeedObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean morePages, Status status) {
            isLoading = false;
            lastStatus = status;
            hasMorePages = morePages;
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleExceptions(Exception e) {
            view.displayMessage("Failed to get feed because of exception: " + e.getMessage());
        }
    }



}

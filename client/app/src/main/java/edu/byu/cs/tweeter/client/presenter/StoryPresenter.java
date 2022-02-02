package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    private final View view;
    private final UserService userService;
    private final StatusService statusService;

    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public StoryPresenter(View view) {
        userService = new UserService();
        statusService = new StatusService();
        this.view = view;
    }

    public interface View {
        void displayMessage(String message);
        void openUserActivity(User user);
        void addItems(List<Status> newStory);
        void setLoadingFooter(boolean loading);
    }

    /**
     * Causes the Adapter to display a loading footer and make a request to get more story
     * data.
     */
    public void loadMoreItems(User user) {
        if (!isLoading) { // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            statusService.GetStory(user, lastStatus, new GetStoryObserver());
        }
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }

    public class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean morePages, Status status) {
            isLoading = false;
            view.setLoadingFooter(false);
            hasMorePages = morePages;
            lastStatus = status;
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void handleExceptions(Exception e) {
            view.displayMessage("Failed to get story because of exception: " + e.getMessage());
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
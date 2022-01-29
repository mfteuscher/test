package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    private View view;
    private UserService userService;
    private StatusService statusService;

    private boolean hasMorePages;
    private Status lastStatus;
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


    public StoryPresenter(View view) {
        userService = new UserService();
        statusService = new StatusService();
        this.view = view;
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }


    /**
     * Causes the Adapter to display a loading footer and make a request to get more story
     * data.
     */
    public void loadMoreItems(User user) {
        isLoading = true;
        statusService.GetStory(user, lastStatus, new GetStoryObserver());
    }

    public interface View {
        void displayMessage(String message);
        void openUserStory(User user);
        void addItems(List<Status> newStory);
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            view.openUserStory(user);
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

    public class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean morePages, Status status) {
            isLoading = false;
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
}

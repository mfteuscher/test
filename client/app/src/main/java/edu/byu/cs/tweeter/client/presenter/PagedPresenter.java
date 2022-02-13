package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    private static final int PAGE_SIZE = 10;

    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;

    private boolean isGettingUser;

    protected PagedPresenter(PagedView<T> view) {
        super(view);
    }

    public void loadMoreItems() {
        if (!isLoading) {  // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            getView().setLoading(true);
            getItems(authToken, targetUser, PAGE_SIZE, lastItem);
        }
    }

    public void getUser(String username) {
        userService.getUser(username, new GetUserObserver());
    }

    protected abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);

    protected abstract String getPagedActionType();

    protected class GetPagedObserver extends Observer implements PagedObserver<T> {

        @Override
        public void handleSuccess(List<T> items, boolean morePages, T item) {
            isLoading = false;
            getView().setLoading(false);
            lastItem = item;
            hasMorePages = morePages;
            getView().addItems(items);
        }

        @Override
        protected String getActionType() {
            return getPagedActionType();
        }
    }

    protected class GetUserObserver extends Observer implements UserObserver {

        @Override
        public void handleSuccess(User user) {
            getView().navigateToUser(user);
        }

        @Override
        protected String getActionType() {
            return "user's profile";
        }

    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private PagedView<T> getView() {
        return (PagedView<T>) view;
    }

}

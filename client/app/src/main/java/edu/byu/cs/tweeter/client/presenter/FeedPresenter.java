package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends StatusPagedPresenter {

    public FeedPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.GetFeed(targetUser, lastItem, pageSize, new GetPagedObserver());
    }

    @Override
    protected String getPagedActionType() {
        return "get feed";
    }

}

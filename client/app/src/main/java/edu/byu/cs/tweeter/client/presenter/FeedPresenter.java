package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    private final StatusService statusService;

    public FeedPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.GetFeed(targetUser, lastItem, new GetPagedObserver());
    }

    @Override
    protected String getDescription() {
        return "get feed";
    }

}

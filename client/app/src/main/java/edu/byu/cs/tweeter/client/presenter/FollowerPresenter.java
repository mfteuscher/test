package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends FollowerPagedPresenter {

    public FollowerPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followerService.GetFollowers(targetUser, lastItem, pageSize, new GetPagedObserver());
    }

    @Override
    protected String getPagedActionType() {
        return "get followers";
    }

}

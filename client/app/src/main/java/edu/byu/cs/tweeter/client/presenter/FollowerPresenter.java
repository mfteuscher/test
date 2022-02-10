package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerPresenter extends PagedPresenter<User> {

    private final FollowerService followerService;

    public FollowerPresenter(PagedView<User> view) {
        super(view);
        this.followerService = new FollowerService();
    }

    @Override
    protected void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followerService.GetFollowers(targetUser, lastItem, pageSize, new GetPagedObserver());
    }

    @Override
    protected String getDescription() {
        return "get followers";
    }

}

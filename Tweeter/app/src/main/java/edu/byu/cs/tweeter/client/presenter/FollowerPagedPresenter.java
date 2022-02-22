package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class FollowerPagedPresenter extends PagedPresenter<User> {

    protected final FollowerService followerService;

    protected FollowerPagedPresenter(PagedView<User> view) {
        super(view);
        followerService = new FollowerService();
    }

}

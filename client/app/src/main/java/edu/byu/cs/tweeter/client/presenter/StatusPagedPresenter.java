package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class StatusPagedPresenter extends PagedPresenter<Status> {

    protected final StatusService statusService;

    protected StatusPagedPresenter(PagedView<Status> view) {
        super(view);
        statusService = new StatusService();
    }
}

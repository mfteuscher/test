package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends Handler {
    private final StatusService.GetFeedObserver observer;

    public GetFeedHandler(StatusService.GetFeedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
        if (success) {
            List<Status> statuses = (List<Status>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            observer.handleSuccess(statuses, hasMorePages, lastStatus);
        } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
            observer.handleExceptions(ex);
        }
    }
}

package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;

// UnfollowHandler
public class UnfollowHandler extends Handler {
    private final FollowerService.UnfollowObserver observer;

    public UnfollowHandler(FollowerService.UnfollowObserver observer) {
        this.observer = observer;
    }
    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
        if (success) {
            observer.handleSuccess();
        } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
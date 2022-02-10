package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowerService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

// GetFollowingCountHandler
public class GetFollowingCountHandler extends Handler {
    private final FollowerService.GetFollowingCountObserver observer;

    public GetFollowingCountHandler(FollowerService.GetFollowingCountObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
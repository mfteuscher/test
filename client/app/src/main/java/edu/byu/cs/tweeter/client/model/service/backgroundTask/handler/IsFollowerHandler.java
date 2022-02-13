package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.iIsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.IsFollowerTask;

// IsFollowerHandler
public class IsFollowerHandler extends BackgroundTaskHandler<iIsFollowerObserver> {

    public IsFollowerHandler(iIsFollowerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, iIsFollowerObserver observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;

// LogoutHandler
public class LogoutHandler extends BackgroundTaskHandler<SimpleNotificationObserver> {

    public LogoutHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, SimpleNotificationObserver observer) {
        Cache.getInstance().clearCache();
        observer.handleSuccess();
    }
}
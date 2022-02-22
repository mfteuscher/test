package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.CountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetCountObserver;

public class GetCountHandler extends BackgroundTaskHandler<GetCountObserver> {

    public GetCountHandler(GetCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetCountObserver observer) {
        int count = data.getInt(CountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}

package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface GetCountObserver extends ServiceObserver {
    void handleSuccess(int count);
}

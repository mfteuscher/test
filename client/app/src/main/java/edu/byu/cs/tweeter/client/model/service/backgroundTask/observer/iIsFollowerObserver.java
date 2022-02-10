package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface iIsFollowerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}

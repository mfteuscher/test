package edu.byu.cs.tweeter.client.model.service.backgroundTask.task;

import android.os.Bundle;
import android.os.Handler;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {
    private static final String LOG_TAG = "IsFollowerTask";

    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private final User follower;
    /**
     * The alleged followee.
     */
    private final User followee;

    private boolean isFollower;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void runTask() {
        isFollower = new Random().nextBoolean();
    }

    @Override
    protected void setBundleData(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}

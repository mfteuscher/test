package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";

    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;
    private int count;

    public CountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void runTask() {
        count = runCountTask();
    }

    protected abstract int runCountTask();

    @Override
    protected void setBundleData(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, 20);
    }
}

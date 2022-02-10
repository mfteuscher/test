package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerService {

    public void isFollower(User selectedUser, MainPresenter.IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(isFollowerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void GetFollowers(User user, User lastFollower, int pageSize, GetFollowersObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void GetFollowing(User user, int pageSize, User lastFollowee,
                             GetFollowingObserver getFollowingObserver) {

        GetFollowingTask getFollowingTask = new GetFollowingTask(
                Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastFollowee,
                new GetFollowingHandler(getFollowingObserver));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void GetFollowingAndFollowerCount(User selectedUser,
                                             GetFollowingCountObserver followingCountObserver,
                                             GetFollowerCountObserver followerCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(followerCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(followingCountObserver));
        executor.execute(followingCountTask);
    }

    public void FollowUser(User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void UnfollowUser(User selectedUser, UnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }

    public interface IsFollowerObserver {
        void handleSuccess(boolean isFollower);
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface GetFollowersObserver {
        void handleSuccess(List<User> followers, boolean morePages, User user);
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface GetFollowingObserver {
        void handleSuccess(List<User> followees, boolean morePages);
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface GetFollowerCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface GetFollowingCountObserver {
        void handleSuccess(int count);
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface FollowObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception e);
    }

    public interface UnfollowObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception e);
    }

}

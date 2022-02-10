package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.Task;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.iIsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowerService {

    public void isFollower(User selectedUser, iIsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(isFollowerObserver));
        Task.executeTask(isFollowerTask);
    }

    public void GetFollowers(User user, User lastFollower, int pageSize, PagedObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new PagedTaskHandler<>(observer));
        Task.executeTask(getFollowersTask);
    }

    public void GetFollowing(User user, int pageSize, User lastFollowee,
                             PagedObserver<User> getFollowingObserver) {

        GetFollowingTask getFollowingTask = new GetFollowingTask(
                Cache.getInstance().getCurrUserAuthToken(), user, pageSize, lastFollowee,
                new PagedTaskHandler<>(getFollowingObserver));

        Task.executeTask(getFollowingTask);
    }

    public void GetFollowingAndFollowerCount(User selectedUser,
                                             GetCountObserver followingCountObserver,
                                             GetCountObserver followerCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followerCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followingCountObserver));
        executor.execute(followingCountTask);
    }

    public void FollowUser(User selectedUser, SimpleNotificationObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        Task.executeTask(followTask);
    }

    public void UnfollowUser(User selectedUser, SimpleNotificationObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        Task.executeTask(unfollowTask);
    }

}

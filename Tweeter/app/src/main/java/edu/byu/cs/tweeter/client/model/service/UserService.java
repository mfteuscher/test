package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.Task;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticateUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.RegisterTask;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public void login(String alias, String password, UserObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticateUserHandler(loginObserver));
        Task.executeTask(loginTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        Task.executeTask(logoutTask);
    }

    public void registerUser(String firstName, String lastName, String alias, String password,
                             String imageBytesBase64, UserObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new AuthenticateUserHandler(observer));

        Task.executeTask(registerTask);
    }

    public void getUser(String username, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                username, new UserHandler(observer));
        Task.executeTask(getUserTask);
    }

    public boolean compareUsers(User selectedUser) {
        return selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0;
    }

}

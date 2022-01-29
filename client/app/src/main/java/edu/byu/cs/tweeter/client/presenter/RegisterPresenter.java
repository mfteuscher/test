package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {

    private View view;
    private UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String image) {
        userService.registerUser(firstName, lastName, alias, password, image, new RegisterObserver());
    }

    public interface View {
        void displayMessage(String message);
        void successfullyRegistered(User registeredUser);
    }

    public class RegisterObserver implements UserService.RegisterObserver {

        @Override
        public void handleSuccess(User user) {
            view.successfullyRegistered(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayMessage("Failed to register because of exception: " + e.getMessage());
        }
    }
}

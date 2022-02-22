package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;

public class RegisterPresenter extends AuthenticationPresenter {

    public RegisterPresenter(AuthenticateView view) {
        super(view);
    }

    @Override
    protected String getAuthenticationActionType() {
        return "register";
    }

    public void registerUser(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {

        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        userService.registerUser(firstName, lastName, alias, password, imageBytesBase64, new AuthenticationObserver());
    }

    public void validateRegistration(Editable firstName, Editable lastName, Editable alias,
                                     Editable password, ImageView imageToUpload) throws IllegalArgumentException {
        validate(alias, password);
        if (isFieldEmpty(firstName)) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (isFieldEmpty(lastName)) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

}

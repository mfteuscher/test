package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class UnitTests {

    @Nested
    @DisplayName("Post Status")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class PostStatusTests {

        MainActivity mockActivity;
        MainPresenter spiedPresenter;
        StatusService mockStatusService;
        String testPost = "A test post";

        @BeforeAll
        public void setUp() {
            mockActivity = Mockito.mock(MainActivity.class);
            mockStatusService = Mockito.mock(StatusService.class);
            spiedPresenter = Mockito.spy(new MainPresenter(mockActivity, mockStatusService));
        }

        @Test
        @DisplayName("successful")
        public void success() {

            Mockito.doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                Assertions.assertEquals(testPost, args[0]);
                Assertions.assertInstanceOf(MainPresenter.PostStatusObserver.class, args[1]);
                ((MainPresenter.PostStatusObserver) args[1]).handleSuccess();
                return null;
            }).when(mockStatusService).PostStatus(Mockito.eq(testPost), Mockito.any());

            spiedPresenter.postStatus(testPost);

            Mockito.verify(mockActivity).displayMessage("Successfully Posted!");

        }

        @Test
        @DisplayName("failed")
        public void failure() {
            String failTest = "Testing Failure";
            Mockito.doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                Assertions.assertEquals(testPost, args[0]);
                Assertions.assertInstanceOf(MainPresenter.PostStatusObserver.class, args[1]);
                ((MainPresenter.PostStatusObserver) args[1]).handleFailure(failTest);
                return null;
            }).when(mockStatusService).PostStatus(Mockito.eq(testPost), Mockito.any());

            spiedPresenter.postStatus(testPost);

            Mockito.verify(mockActivity).displayMessage("Failed to post status: " + failTest);
        }

        @Test
        @DisplayName("threw an exception")
        public void exception() {
            Exception e = new Exception();
            Mockito.doAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                Assertions.assertEquals(testPost, args[0]);
                Assertions.assertInstanceOf(MainPresenter.PostStatusObserver.class, args[1]);
                ((MainPresenter.PostStatusObserver) args[1]).handleException(e);
                return null;
            }).when(mockStatusService).PostStatus(Mockito.eq(testPost), Mockito.any());

            spiedPresenter.postStatus(testPost);

            Mockito.verify(mockActivity).displayMessage("Failed to post status because of exception: " + e.getMessage());

        }

    }

}
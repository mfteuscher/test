package edu.byu.cs.tweeter.client.model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.Task;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.PostStatusTask;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void PostStatus(String post, SimpleNotificationObserver postStatusObserver) {
        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(),
                    getFormattedDateTime(), parseURLs(post), parseMentions(post));

            PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                    newStatus, new SimpleNotificationHandler(postStatusObserver));

            Task.executeTask(statusTask);
        } catch (Exception ex) {
            postStatusObserver.handleException(ex);
        }
    }

    public void GetStory(User user, Status lastStatus, int pageSize, PagedObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(observer));
        Task.executeTask(getStoryTask);
    }

    public void GetFeed(User user, Status lastStatus, int pageSize, PagedObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedTaskHandler<>(observer));
        Task.executeTask(getFeedTask);
    }

    private String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    private List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private int findUrlEndIndex(String word) {
        String[] urlEndIndexes = {".com", ".org", ".edu", ".net", ".mil"};
        for (String endIndex : urlEndIndexes) {
            if (word.contains(endIndex)) {
                int index = word.indexOf(endIndex);
                index += 4;
                return index;
            }
        }
        return word.length();
    }

}

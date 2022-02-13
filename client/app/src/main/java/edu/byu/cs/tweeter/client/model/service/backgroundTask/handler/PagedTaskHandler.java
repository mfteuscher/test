package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.PagedTask;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedObserver<T>> {

    public PagedTaskHandler(PagedObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver<T> observer) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
        T lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        observer.handleSuccess(items, hasMorePages, lastItem);
    }

}

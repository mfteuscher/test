package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.task.BackgroundTask;

public class Task {
    public static void executeTask(BackgroundTask task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}

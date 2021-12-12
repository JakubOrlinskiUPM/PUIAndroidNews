package com.example.puiandroidnews;

import com.example.puiandroidnews.exceptions.ServerCommunicationError;

import java.util.List;

public class GetArticleTask implements Runnable {
    MainActivity activity;

    GetArticleTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            List<Article> res = MainActivity.modelManager.getArticles();
            activity.runOnUiThread(() -> {
                activity.receiveData(res);
            });

        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
    }
}

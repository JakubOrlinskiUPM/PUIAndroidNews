package com.example.puiandroidnews;

import com.example.puiandroidnews.exceptions.ServerCommunicationError;

import java.util.List;

public class GetArticleTask implements Runnable {
    ModelManager modelManager;
    MainActivity activity;

    GetArticleTask(ModelManager modelManager, MainActivity activity) {
        this.modelManager = modelManager;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            List<Article> res = modelManager.getArticles();
            activity.runOnUiThread(() -> {
                activity.receiveData(res);
            });

        } catch (ServerCommunicationError serverCommunicationError) {
            serverCommunicationError.printStackTrace();
        }
    }
}

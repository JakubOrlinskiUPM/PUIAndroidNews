package com.example.puiandroidnews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puiandroidnews.exceptions.AuthenticationError;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    static String PARAM_ARTICLE = "article";

    ModelManager modelManager;
    ArticleAdapter adapter;
    List<Article> data;

    List<String> tabs = Arrays.asList("All", "National", "International", "Sport", "Economy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupArticleData();

        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        for (String tab: tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String currentTab = tabs.get(tab.getPosition());
                adapter.getFilter().filter(currentTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    void setupArticleData() {
        Properties props = new Properties();
        props.setProperty(ModelManager.ATTR_SERVICE_URL, "https://sanger.dia.fi.upm.es/pmd-task/");
        try {
            modelManager = new ModelManager(props);

            GetArticleTask task = new GetArticleTask(modelManager, this);
            new Thread(task).start();

        } catch (AuthenticationError authenticationError) {
            authenticationError.printStackTrace();
        }
        adapter = new ArticleAdapter(this);
        ((ListView) findViewById(R.id.articleListView)).setAdapter(adapter);
    }

    public void receiveData(List<Article> data) {
        this.data = data;
        adapter.setData(data);
    }

    public void routeToArticle(Article article) {
        Intent intent = new Intent(getApplicationContext(), ShowArticleActivity.class);
        intent.putExtra(PARAM_ARTICLE, article.getId());
        startActivity(intent);
    }

}
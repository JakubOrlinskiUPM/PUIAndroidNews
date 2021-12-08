package com.example.puiandroidnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puiandroidnews.exceptions.AuthenticationError;
import com.example.puiandroidnews.exceptions.ServerCommunicationError;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    static String PARAM_ARTICLE = "article";

    ModelManager modelManager;
    ArticleAdapter adapter;
    List<Article> data;
    static Boolean loggedIn = false;
    private String userID = "";
    private String apiKey = "";

    List<String> tabs = Arrays.asList("All", "National", "International", "Sport", "Economy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupArticleData();

        // Tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
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

            // check if user wanted his authentication to be remembered
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (prefs.getBoolean(LoginActivity.KEY_BOOLEAN, false)) {
                // he authenticated before
                if (prefs.contains(LoginActivity.KEY_API) && prefs.contains(LoginActivity.KEY_USERNAME)) {
                    loggedIn = true;
                    this.userID = prefs.getString(LoginActivity.KEY_USERNAME, "");
                    this.apiKey = prefs.getString(LoginActivity.KEY_API, "");
                    String password = prefs.getString(LoginActivity.KEY_PASSWORD, "");
                    props.setProperty(ModelManager.ATTR_LOGIN_USER, this.userID);
                    props.setProperty(ModelManager.ATTR_LOGIN_PASS, password);
                    // TODO: Not sure how having the apiKey helps, since its not an attribute for ModelManager?
                }
            }

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
        //Intent intent = new Intent(getApplicationContext(), EditArticleActivity.class);
        Intent intent = new Intent(getApplicationContext(), ShowArticleActivity.class);
        intent.putExtra(PARAM_ARTICLE, article.getId());
        startActivity(intent);
    }

    // LOGIN FUNCTIONALITY

    public void login(View view) {
        if (!loggedIn) {
            // allow user to log in
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            // log out user
            loggedIn = false;
            updateLabelsRegardingLoginStatus();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor ed = prefs.edit();
            ed.remove(LoginActivity.KEY_USERNAME);
            ed.remove(LoginActivity.KEY_PASSWORD);
            ed.remove(LoginActivity.KEY_API);
            ed.putBoolean(LoginActivity.KEY_BOOLEAN, false);
            ed.apply();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLabelsRegardingLoginStatus();
    }

    @SuppressLint("SetTextI18n")
    private void updateLabelsRegardingLoginStatus() {
        if (loggedIn) {
            // change button label
            Button loginButton = findViewById(R.id.loginButton);
            loginButton.setText("Log out");
            // change status
            TextView loginStatus = findViewById(R.id.loginStatus);
            loginStatus.setText("You are logged in!" );
        } else {
            // change button label
            Button loginButton = findViewById(R.id.loginButton);
            loginButton.setText("Log in");
            // change status
            TextView loginStatus = findViewById(R.id.loginStatus);
            loginStatus.setText("Currently not logged in!" );
        }
    }
}
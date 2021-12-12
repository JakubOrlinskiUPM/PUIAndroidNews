package com.example.puiandroidnews;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puiandroidnews.exceptions.AuthenticationError;
import com.example.puiandroidnews.exceptions.ServerCommunicationError;

import org.json.simple.parser.JSONParser;

import java.util.Properties;

public class ShowArticleActivity extends AppCompatActivity {

    private static final String PARAM_ARTICLE = "article";
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView article_title = findViewById(R.id.titleId);
        ImageView article_image = findViewById(R.id.imageId);
        TextView article_category = findViewById(R.id.categoryId);
        TextView article_abstract = findViewById(R.id.abstractId);
        TextView article_body = findViewById(R.id.bodyId);
        TextView userId = findViewById(R.id.userId);

        Intent intent = getIntent();
        String articleTitle = intent.getStringExtra("articleTitle");
        String articleAbstract = intent.getStringExtra("articleAbstract");
        String articleCategory = intent.getStringExtra("articleCategory");
        String articleBody = intent.getStringExtra("articleBody");
        String articleImage = intent.getStringExtra("articleImage");
        int articleUser = intent.getIntExtra("articleUser", 0);

        article_title.setText(articleTitle);
        article_abstract.setText(Html.fromHtml(articleAbstract, Html.FROM_HTML_MODE_COMPACT));
        article_body.setText(Html.fromHtml(articleBody, Html.FROM_HTML_MODE_COMPACT));
        article_category.setText(articleCategory);
        //if(articleUser > 0) { userId.setBackgroundResource(articleUser); }

        article_image.setImageBitmap(Utils.base64StringToImg(articleImage));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
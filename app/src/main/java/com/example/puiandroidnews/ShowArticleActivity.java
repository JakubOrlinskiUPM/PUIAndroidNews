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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        int id = intent.getIntExtra(PARAM_ARTICLE, -1);

        DownloadArticleThread d = new DownloadArticleThread(this, id);

        Thread th = new Thread (d);
        th.start();
    }

    protected void initialize(Article article) throws ServerCommunicationError {
        TextView article_title = findViewById(R.id.titleId);
        ImageView article_image = findViewById(R.id.imageId);
        TextView article_category = findViewById(R.id.categoryId);
        TextView article_abstract = findViewById(R.id.abstractId);
        TextView article_body = findViewById(R.id.bodyId);
        TextView userId = findViewById(R.id.userId);

        article_title.setText(article.getTitleText());
        article_abstract.setText(Html.fromHtml(article.getAbstractText(), Html.FROM_HTML_MODE_COMPACT));
        article_body.setText(Html.fromHtml(article.getBodyText(), Html.FROM_HTML_MODE_COMPACT));
        article_category.setText(article.getCategory());
        /**if(article.getIdUser() > 0) {
         userId.setText(article.getIdUser());
         }*/
        if(article.getImage().getImage() != null){
            article_image.setImageBitmap(Utils.base64StringToImg(article.getImage().getImage()));
        } else {
            article_image.setImageResource(R.drawable.fallback_article_image);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
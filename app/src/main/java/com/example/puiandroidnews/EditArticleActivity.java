package com.example.puiandroidnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puiandroidnews.exceptions.AuthenticationError;
import com.example.puiandroidnews.exceptions.ServerCommunicationError;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class EditArticleActivity extends AppCompatActivity {

    private static final int CODE_OPEN_IMAGE = 1;
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        Intent intent = getIntent();
        int articleId = intent.getIntExtra(MainActivity.PARAM_ARTICLE,-1);

        TextView title = findViewById(R.id.text_title);
        TextView articleAbstract = findViewById(R.id.text_abstarct);
        TextView body = findViewById(R.id.text_body);
        TextView category = findViewById(R.id.text_category);
        ImageView image = findViewById(R.id.image_edit);

        // Make sure we connect in the background and not in the main thread
        Thread thread = new Thread(() -> {
            try {
                article = MainActivity.modelManager.getArticle(articleId);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        title.setText(article.getTitleText());
                        articleAbstract.setText(Html.fromHtml(article.getAbstractText(), Html.FROM_HTML_MODE_COMPACT));
                        body.setText(Html.fromHtml(article.getBodyText(), Html.FROM_HTML_MODE_COMPACT));
                        category.setText(article.getCategory());


                        System.out.println(article.toString());

                        Bitmap bitmap = null;
                        try {
                            Image img = article.getImage();
                            if (img != null) {
                                String str = img.getImage();
                                if (str != null) {
                                    bitmap = stringToBitMap(str);
                                }
                            }
                        } catch (ServerCommunicationError serverCommunicationError) {
                            System.out.println("oh no");
                        }
                        if (bitmap == null) {
                            image.setImageResource(R.drawable.fallback_article_image);
                        } else {
                            image.setImageBitmap(bitmap);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();

        Button btn = findViewById(R.id.btn_change_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, CODE_OPEN_IMAGE);
            }
        });

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(() -> {
                    try {
                        MainActivity.modelManager.save(article);

                        Intent intent = new Intent(getApplicationContext(), ShowArticleActivity.class);
                        intent.putExtra(MainActivity.PARAM_ARTICLE, article.getId());
                        startActivity(intent);

                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_OPEN_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    InputStream stream = null;
                    try{
                        stream =getContentResolver().openInputStream(data.getData());
                        Bitmap bmp =Utils.createScaledImage( BitmapFactory.decodeStream(stream),500,500);
                        article.addImage(Utils.imgToBase64String(bmp), "");

                        ImageView viewer = findViewById(R.id.image_edit);
                        viewer.setImageBitmap(bmp);
                    }catch(FileNotFoundException | ServerCommunicationError e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(this, "Action cancelled by user", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
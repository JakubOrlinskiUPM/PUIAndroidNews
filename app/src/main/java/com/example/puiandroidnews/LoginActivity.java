package com.example.puiandroidnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.puiandroidnews.exceptions.AuthenticationError;

import java.util.Properties;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAttempt(View view) {
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        // check if correct credentials
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Properties props = new Properties();
        props.setProperty(ModelManager.ATTR_SERVICE_URL, "https://sanger.dia.fi.upm.es/pmd-task/");
        props.setProperty(ModelManager.ATTR_LOGIN_USER, username);
        props.setProperty(ModelManager.ATTR_LOGIN_PASS, password);

        // Make sure we connect in the background and not in the main thread
        Thread thread = new Thread(() -> {
            try {
                ModelManager modelManager = new ModelManager(props);
            } catch (AuthenticationError authenticationError) {
                authenticationError.printStackTrace();
                // add toast
                Toast.makeText(LoginActivity.this, "Invalid credentials!",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();


        // successful login, since no authentication error
        MainActivity.loggedIn = true;
        System.out.println("Successfully logged in.");
        finish();

    }

    //TODO: add remember me functionality and use remote authentication
}
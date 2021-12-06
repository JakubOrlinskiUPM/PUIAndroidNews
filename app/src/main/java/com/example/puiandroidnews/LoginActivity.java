package com.example.puiandroidnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        if (username.equals("user") && password.equals("password")) {
            MainActivity.loggedIn = true;
            System.out.println("Successfully logged in.");
            finish();
        } else {
            // add toast
            Toast.makeText(LoginActivity.this, "Invalid credentials!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: add remember me functionality and use remote authentication
}
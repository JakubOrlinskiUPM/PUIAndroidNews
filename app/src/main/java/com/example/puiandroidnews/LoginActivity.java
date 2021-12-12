package com.example.puiandroidnews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puiandroidnews.exceptions.AuthenticationError;

public class LoginActivity extends AppCompatActivity {


    private String userID;
    private String apiKey;
    public static String KEY_BOOLEAN = "rememberBoolean";
    public static String KEY_API= "apiKey";
    public static String KEY_USERNAME= "usernameKey";
    public static String KEY_PASSWORD = "passwordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userID = "";
        apiKey = "";
    }

    public void loginAttempt(View view) {
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        // check if correct credentials
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

//        // Make sure we connect in the background and not in the main thread
        Thread thread = new Thread(() -> {
            try {
                MainActivity.modelManager.login(username, password);

                // successful login, since no authentication error
                MainActivity.loggedIn = true;
                System.out.println("Successfully logged in.");

                runOnUiThread(this::finish);
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
    }

    public void rememberMeClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(KEY_BOOLEAN, checked);
        ed.apply();
    }

    //TODO: add remember me functionality and use remote authentication
}
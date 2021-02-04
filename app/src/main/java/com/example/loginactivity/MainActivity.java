package com.example.loginactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginactivity.db.AppDatabase;
import com.example.loginactivity.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private static final String USER_ID_KEY = "com.example.loginactivity.db.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.loginactivity.db.PREFERENCES_KEY";

    private TextView mWelcomeText;

    private Button mLogoutBtn;

    private User mUser;
    private UserDAO mUserDAO;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        wireUpDisplay();
        checkForUser();
        addUserToPrefs(mUserId);

        loginUser(mUserId);

        refreshDisplay();

    }

    private void wireUpDisplay(){
        mWelcomeText = findViewById(R.id.textViewWelcomeMsg);
        mLogoutBtn = findViewById(R.id.logoutBtn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void checkForUser(){
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){ return; }

        if(mPreferences == null){ getPrefs(); }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){ return; }

        List<User> users = mUserDAO.getAllUsers();

        if(users.size() <= 0){
            User defaultUser = new User("default", "default");
            User altUser = new User("alt", "alt");
            mUserDAO.insert(defaultUser, altUser);
        }

        // Go to Login Screen
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void loginUser(int userId){
        mUser = mUserDAO.getUserByUserId(userId);
        mWelcomeText.setText("Welcome " + mUser.getUsername());
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPrefs();
                        mUserId = -1;
                        checkForUser();
                    }
                });

        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Don't need to do anything here
                        snackMaker("You clicked NO");
                    }
                });

        alertBuilder.create().show();
    }

    private void getPrefs(){ mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE); }

    private void addUserToPrefs(int userId){
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void clearUserFromPrefs(){ addUserToPrefs(-1); }

    private void clearUserFromIntent(){ getIntent().putExtra(USER_ID_KEY, -1); }

    private void refreshDisplay(){
        // use this for the API call
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.USER_TABLE).allowMainThreadQueries().build().getUserDAO();
    }

    private void snackMaker(String message){
        Snackbar snackBar = Snackbar.make(findViewById(R.id.layoutMainActivity),
                message,
                Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}
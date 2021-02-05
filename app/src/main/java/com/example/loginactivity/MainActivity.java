package com.example.loginactivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginactivity.db.AppDatabase;
import com.example.loginactivity.db.UserDAO;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    
    private static final String USER_ID_KEY = "com.example.loginactivity.db.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.loginactivity.db.PREFERENCES_KEY";

    private TextView mWelcomeText;
    private TextView mMainDisplay;

    private Button mLogoutBtn;

    private Retrofit mRetrofit;

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

        if(mUserId != -1){
            refreshWelcome();
            callAllPosts();
        }

    }

    private void callAllPosts(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        Call<List<Post>> call = jsonPlaceHolderAPI.getAllPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    mMainDisplay.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for(Post post : posts){
                    if(post.getUserId() == mUser.getUserId()){
                        String content = "";
                        content += "ID: " + post.getId() + "\n";
                        content += "User ID: " + post.getUserId() + "\n";
                        content += "Title: " + post.getTitle() + "\n";
                        content += "Text: " + post.getText() + "\n\n";
                        mMainDisplay.append(content);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                mMainDisplay.setText(t.getMessage());
            }
        });
    }

    private void wireUpDisplay(){
        mWelcomeText = findViewById(R.id.textViewWelcomeMsg);
        mMainDisplay = findViewById(R.id.mainDisplay);
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        mLogoutBtn = findViewById(R.id.logoutBtn);
        mLogoutBtn.setOnClickListener(v -> logoutUser());
    }

    private void checkForUser(){
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){ return; }

        if(mPreferences == null){ getPrefs(); }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){ return; }

        List<User> users = mUserDAO.getAllUsers();

        if(users.size() <= 0){
            User dinUser = new User("din_djarin", "baby_yoda_ftw");
            User defaultUser = new User("default", "default");
            if(dinUser.getUserId() != 1){ dinUser.setUserId(1); }
            mUserDAO.insert(dinUser, defaultUser);
        }

        // Go to Login Screen
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void loginUser(int userId){
        mUser = mUserDAO.getUserByUserId(userId);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Yes", (dialog, which) -> {
            clearUserFromIntent();
            clearUserFromPrefs();
            mUserId = -1;
            checkForUser();
        });

        alertBuilder.setNegativeButton("No", (dialog, which) -> {
                    //Don't need to do anything here
                    snackMaker("You clicked NO");
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

    private void refreshWelcome(){
        mWelcomeText.setText("Welcome " + mUser.getUsername());
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

    // Make a test for this and check if the extra value is present
    // if(intent.getExtra() == int) passes
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}
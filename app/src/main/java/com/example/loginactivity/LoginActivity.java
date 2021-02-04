package com.example.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginactivity.db.AppDatabase;
import com.example.loginactivity.db.UserDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButton;

    private User mUser;

    private UserDAO mUserDAO;

    private String mUsernameString;
    private String mPasswordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUpDisplay();

        getDatabase();

    }

    private void wireUpDisplay(){
        mUsernameField = findViewById(R.id.editTextLoginUserName);
        mPasswordField = findViewById(R.id.editTextLoginPassword);

        mButton = findViewById(R.id.buttonLogin);
        mButton.setOnClickListener(v -> {
            getValuesFromDisplay();
            if(checkForUser()){
                if(validatePassword()){
                    Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                    startActivity(intent);
                } else {
                   Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getValuesFromDisplay(){
        mUsernameString = mUsernameField.getText().toString();
        mPasswordString = mPasswordField.getText().toString();
    }

    private boolean checkForUser(){
        mUser = mUserDAO.getUserByUsername(mUsernameString);

        for(User user : mUserDAO.getAllUsers()){
            Log.d("Username", user.getUsername());
        }

        if(mUser == null){
            Toast.makeText(this, "No user " + mUsernameString + " with password " + mPasswordString +" found", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validatePassword(){ return mUser.getPassword().equals(mPasswordString);    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.USER_TABLE)
                .allowMainThreadQueries()
                .build()
                .getUserDAO();

    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}
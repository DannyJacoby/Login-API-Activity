package com.example.loginactivity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.loginactivity.db.AppDatabase;

@Entity(tableName = "user_table")
public class User {

        @PrimaryKey(autoGenerate = true)
        @NonNull
        private int mUserId;

        private String mUsername;
        private String mPassword;

        public User(String mUsername, String mPassword){
                this.mUsername = mUsername;
                this.mPassword = mPassword;
        }


        public int getUserId() {
                return mUserId;
        }

        public void setUserId(int mUserId) {
                this.mUserId = mUserId;
        }

        public String getUsername() {
                return mUsername;
        }

        public void setUsername(String mUsername) {
                this.mUsername = mUsername;
        }

        public String getPassword() {
                return mPassword;
        }

        public void setPassword(String mPassword) {
                this.mPassword = mPassword;
        }
}

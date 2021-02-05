package com.example.loginactivity.db;

import android.view.ViewDebug;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.loginactivity.User;
import com.example.loginactivity.db.typeConverters.DateTypeConverter;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DB_NAME = "db_name";
    public static final String USER_TABLE = "user_table";
    public abstract UserDAO getUserDAO();

}

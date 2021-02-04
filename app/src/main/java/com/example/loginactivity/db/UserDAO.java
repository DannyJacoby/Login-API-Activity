package com.example.loginactivity.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.loginactivity.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE mUsername = :username")
    User getUserByUsername(String username);
    
    @Query("SELECT * FROM user_table WHERE mUserId = :userId")
    User getUserByUserId(int userId);
}

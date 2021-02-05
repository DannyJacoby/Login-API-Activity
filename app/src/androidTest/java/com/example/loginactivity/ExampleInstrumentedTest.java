package com.example.loginactivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.loginactivity.db.AppDatabase;
import com.example.loginactivity.db.UserDAO;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // Test Factory stuff, verification of passwords and username

    @Test
    public void factoryCreationTest(){
        Context testContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent testIntent = MainActivity.intentFactory(testContext, 5);

        assertEquals(5, testIntent.getIntExtra("com.example.loginactivity.db.userIdKey", -1));
    }

    @Test
    public void userInsertTest(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserDAO myTestDB = Room.databaseBuilder(appContext, AppDatabase.class, AppDatabase.USER_TABLE)
                .allowMainThreadQueries().build().getUserDAO();

        User user = new User("danny", "BitchLasagna");

        User user2 = myTestDB.getUserByUsername(user.getUsername());

        if(myTestDB.getUserByUsername(user.getUsername()) == null){
            myTestDB.insert(user);
        }

        if(user2 == null){
            user2 = myTestDB.getUserByUsername(user.getUsername());
        }

        assertEquals(user, user2);
    }

    @Test
    public void testUsername(){
        User user = new User("danny", "test");
        assertEquals("danny", user.getUsername());
    }

    @Test
    public void testPassword(){
        User user = new User("danny", "test");
        assertEquals("test", user.getPassword());
    }

}
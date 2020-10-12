package com.hfad.nationalparksguide.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ParkInfo.class, Comment.class}, version = 2, exportSchema = false)


public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "my_db";

    private static AppDatabase databaseInstance;

    public static synchronized AppDatabase getInstance(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return databaseInstance;
    }

    public abstract ParkInfoDao ParkInfoDao();
    public abstract CommentDao CommentDao();
}


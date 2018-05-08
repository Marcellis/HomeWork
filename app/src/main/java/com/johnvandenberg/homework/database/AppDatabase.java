package com.johnvandenberg.homework.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.johnvandenberg.homework.database.dao.HomeworkDao;
import com.johnvandenberg.homework.database.entity.Homework;

@Database(entities = {Homework.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract HomeworkDao homeworkDao();

    // To use the RoomDatabase we need the Singleton design pattern
    // The RoomDatabase is very expensive
    // Information: https://developer.android.com/training/data-storage/room/
    public static AppDatabase getInstance(Context context) {
        if( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "homework-database")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}

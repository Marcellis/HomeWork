package com.johnvandenberg.homework.database.dao;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.johnvandenberg.homework.database.entity.Homework;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface HomeworkDao {

    @Insert
    void insertAll(Homework... homework);

    @Insert
    long insert(Homework homework);

    @Update(onConflict = REPLACE)
    int update(Homework homework);

    @Delete
    void delete(Homework homework);

    @Query("DELETE FROM " + Homework.TABLE_NAME + " WHERE " + Homework.COLUMN_ID + " = :uid")
    int deleteByUid(long uid);

    // Custom queries!
    @Query("SELECT * FROM "+Homework.TABLE_NAME)
    List<Homework> getAll();

    @Query("SELECT * FROM "+Homework.TABLE_NAME+" WHERE "+Homework.COLUMN_ID+" LIKE :uid LIMIT 1")
    Homework findByUid( long uid );

    @Query("SELECT * FROM "+Homework.TABLE_NAME+" WHERE "+Homework.COLUMN_FINISHED+" = :finished")
    List<Homework> findAllByFinished( int finished );

    // Cursors
    @Query("SELECT * FROM " + Homework.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + Homework.TABLE_NAME + " WHERE " + Homework.COLUMN_ID + " = :uid")
    Cursor selectByUid(long uid);

    @Query("SELECT * FROM "+ Homework.TABLE_NAME +" ORDER BY "+ Homework.COLUMN_ID +" ASC")
    DataSource.Factory<Integer, Homework> getAllByUid();

    @Query("SELECT * FROM "+ Homework.TABLE_NAME +" ORDER BY date("+ Homework.COLUMN_DATE +") ASC")
    DataSource.Factory<Integer, Homework> getAllByDateAsc();
}

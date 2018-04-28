package com.johnvandenberg.homework.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.johnvandenberg.homework.data.model.Homework;

import java.util.List;

@Dao
public interface HomeworkDao {

    @Insert
    void insertAll(Homework... homework);

    @Delete
    void delete(Homework homework);

    // Custom queries!
    @Query("SELECT * FROM homework")
    List<Homework> getAll();

    @Query("SELECT * FROM homework WHERE uid LIKE :uid LIMIT 1")
    Homework findByUid( int uid );

    @Query("SELECT * FROM homework WHERE finished = :finished")
    List<Homework> findAllByFinished( int finished );
}

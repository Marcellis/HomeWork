package com.johnvandenberg.homework.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Homework {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "finished")
    private int finished;
}

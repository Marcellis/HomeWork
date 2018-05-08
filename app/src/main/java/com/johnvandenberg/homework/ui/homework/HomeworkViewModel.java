package com.johnvandenberg.homework.ui.homework;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.johnvandenberg.homework.database.dao.HomeworkDao;
import com.johnvandenberg.homework.database.entity.Homework;

public class HomeworkViewModel extends ViewModel {

    public final LiveData<PagedList<Homework>> homeworkList;

    public HomeworkViewModel(HomeworkDao homeworkDao) {
        homeworkList = new LivePagedListBuilder<>( homeworkDao.getAllByDateAsc(), 20 ).build();
    }
}

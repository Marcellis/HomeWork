package com.johnvandenberg.homework.ui.homework;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.johnvandenberg.homework.database.dao.HomeworkDao;

public class HomeworkViewModelFactory implements ViewModelProvider.Factory {

    HomeworkDao homeworkDao;

    public HomeworkViewModelFactory(HomeworkDao homeworkDao) {
        this.homeworkDao = homeworkDao;
    }

    @NonNull
    @Override
    public HomeworkViewModel create(@NonNull Class modelClass) {
        return new HomeworkViewModel( homeworkDao );
    }
}

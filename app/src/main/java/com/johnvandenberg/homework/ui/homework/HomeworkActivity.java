package com.johnvandenberg.homework.ui.homework;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.database.AppDatabase;
import com.johnvandenberg.homework.database.entity.Homework;
import com.johnvandenberg.homework.ui.modify.HomeworkModifyActivity;

public class HomeworkActivity extends AppCompatActivity implements HomeworkAdapter.HomeClickListener  {

    private HomeworkAdapter adapter;

    private RecyclerView recyclerView;

    private static final int LOADER_HOMEWORK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        // Instantiate an item animator for our RecyclerView so each row will get animated when
        // added or removed
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200L);
        itemAnimator.setRemoveDuration(200L);

        HomeworkViewModelFactory homeworkViewModelFactory = new HomeworkViewModelFactory( AppDatabase.getInstance( getApplicationContext() ).homeworkDao() );
        HomeworkViewModel viewModel = ViewModelProviders.of( this, homeworkViewModelFactory ).get( HomeworkViewModel.class );

        // Setup the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new HomeworkAdapter( this );
        viewModel.homeworkList.observe(this, pagedList -> adapter.submitList( pagedList ) );
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(itemAnimator);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> insertHomework());
    }

    @Override
    public void onResume() {
        super.onResume();

//        getSupportLoaderManager().restartLoader( LOADER_HOMEWORK, null, mLoaderCallbacks );
    }

    /**
     * Starting HomeWorkModify with the given Homework object
     * @param homework Homework object
     */
    private void updateHomework(Homework homework) {
        Intent intent = new Intent(this, HomeworkModifyActivity.class);
        intent.putExtra("homeworkUid", homework.getUid() );
        intent.setAction(Intent.ACTION_EDIT);
        startActivity(intent);
    }

    /**
     *
     */
    private void insertHomework() {
        Intent intent = new Intent(this, HomeworkModifyActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        startActivity(intent);
    }

    /**
     * Easy function to delete homework
     * @param homework Homework object
     */
    private void deleteHomework(final Homework homework) {
        // Delete the homework
        AsyncTask.execute( new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance( getApplicationContext() ).homeworkDao().delete( homework );
            }
        });

//        getSupportLoaderManager().restartLoader( LOADER_HOMEWORK, null, mLoaderCallbacks );
        Toast.makeText(this, R.string.message_homework_deleted, Toast.LENGTH_LONG).show();
    }

    @Override
    public void homeOnLongClick(Homework homework) {
        deleteHomework(homework);
    }

    @Override
    public void homeOnClick(Homework homework) {
        updateHomework(homework);
    }
}

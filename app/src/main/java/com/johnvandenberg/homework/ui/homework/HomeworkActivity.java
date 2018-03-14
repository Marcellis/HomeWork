package com.johnvandenberg.homework.ui.homework;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.data.HomeworkRepository;
import com.johnvandenberg.homework.data.local.HomeworkContract;
import com.johnvandenberg.homework.data.provider.HomeworkProvider;
import com.johnvandenberg.homework.data.model.Homework;
import com.johnvandenberg.homework.ui.modify.HomeworkModifyActivity;

public class HomeworkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, HomeworkAdapter.HomeClickListener  {

    private HomeworkAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        // Initialize the loader
        getSupportLoaderManager().initLoader(0, null, this);
        // Instantiate the GamesAdapter and listen for click events on a game object

         // Instantiate an item animator for our RecyclerView so each row will get animated when
        // added or removed
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(200L);
        itemAnimator.setRemoveDuration(200L);
        // Setup the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(itemAnimator);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertHomework();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(
                this,
                HomeworkContract.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (adapter == null) {
            adapter = new HomeworkAdapter ( cursor, this );
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // The cursor's data is now invalid and we should also remove everything from the adapter
        adapter.swapCursor(null);

    }

    private void updateHomework(Homework homework) {
        Intent intent = new Intent(this, HomeworkModifyActivity.class);
        intent.putExtra("homework", homework);
        intent.setAction(Intent.ACTION_EDIT);
        startActivity(intent);
    }

    private void insertHomework() {
        Intent intent = new Intent(this, HomeworkModifyActivity.class);
        intent.setAction(Intent.ACTION_INSERT);
        startActivity(intent);
    }

    private void deleteHomework(Homework homework) {
        HomeworkRepository repository = new HomeworkRepository(getContentResolver());
        repository.delete(homework.getId());
        getSupportLoaderManager().restartLoader(0, null, this);
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

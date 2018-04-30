package com.johnvandenberg.homework.ui.modify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.database.AppDatabase;
import com.johnvandenberg.homework.database.entity.Homework;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class HomeworkModifyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText inputTitle;
    private TextInputEditText inputDate;
    private TextInputEditText inputSubject;
    private CheckBox checkBox;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Homework homework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_modify);
        initializeView();
    }

    protected void initializeView() {
        // Find all our views from within our layout
        inputTitle = findViewById(R.id.inputHomeworkTitle);
        inputDate = findViewById(R.id.inputHomeworkDate);
        inputSubject = findViewById(R.id.inputHomeworkSubject);
        checkBox = findViewById(R.id.checkbox);

        // Make sure the date field can not be edited
        inputDate.setKeyListener(null);
        // Show a date picker when we click on the date field, make sure we set it on the input
        // layout as this is the container around our EditText
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Find our FloatingActionButton and tell it to save the homework object when clicked
        FloatingActionButton fab = findViewById(R.id.fab);

        // Based on the action we will add or update a homework
        if (Objects.equals(getIntent().getAction(), Intent.ACTION_INSERT)) {
            // We are adding a new homework
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveHomework();
                }
            });

        } else {
            // We are updating an existing homework so start by retrieving it from the intent
            final long homeworkUid = getIntent().getLongExtra("homeworkUid", 0 );

            if( homeworkUid > 0 ) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        homework = AppDatabase.getInstance( getApplicationContext() ).homeworkDao().findByUid( homeworkUid );

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setVariablesFromHomework( homework );
                            }
                        });
                    }
                });

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateHomework();
                    }
                });
            }
        }
    }

    private void setVariablesFromHomework( Homework homework ) {
        // Set the values for the views
        inputTitle.setText(homework.getTitle());
        inputDate.setText(homework.getDate());
        inputSubject.setText(homework.getSubject());
        checkBox.setChecked(homework.isFinished());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        String date = dateFormat.format(calendar.getTime());
        inputDate.setText(date);
    }

    private void showDatePicker() {
        String date = inputDate.getText().toString();
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;

        try {
            Date parsedDate = dateFormat.parse(date);
            calendar.setTime(parsedDate);
        } catch (ParseException ignored) {
        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, this, year, month, day).show();
    }

    /**
     * Takes the values from the view and attempts to save a homework entity within the database.
     * The title and platform input values are required and will result in an error message when
     * empty.
     */
    private void saveHomework() {
        String title = inputTitle.getText().toString();
        String date = inputDate.getText().toString();
        String subject = inputSubject.getText().toString();
        boolean finished = checkBox.isChecked();

        if (title.isEmpty()) {
            inputTitle.setError(getString(R.string.error_title_required));
        } else if (subject.isEmpty()) {
            inputSubject.setError(getString(R.string.error_subject_required));
        } else {
            final Homework homework = new Homework(title, subject, date, finished);

            // Insert the homework object in the database
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance( getApplicationContext() ).homeworkDao().insertAll( homework );
                }
            });

            Toast.makeText(this, R.string.message_homework_saved, Toast.LENGTH_LONG).show();

            finish();
        }
    }

    /**
     * Takes the values from the view and attempts to update the homework entity within the database.
     * The title and platform input values are required and will result in an error message when
     * empty.
     */
    private void updateHomework() {
        String title = inputTitle.getText().toString();
        String date = inputDate.getText().toString();
        String subject = inputSubject.getText().toString();
        boolean finished = checkBox.isChecked();

        // Validate that the title and platform is not empty
        if (title.isEmpty()) {
            inputTitle.setError(getString(R.string.error_title_required));
        } else if (subject.isEmpty()) {
            inputSubject.setError(getString(R.string.error_subject_required));
        } else {
            homework.setTitle(title);
            homework.setSubject(subject);
            homework.setDate(date);
            homework.setFinished(finished);

            // Update object in the database
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance( getApplicationContext() ).homeworkDao().update( homework );
                }
            });

            Toast.makeText(this, R.string.message_homework_updated, Toast.LENGTH_LONG).show();

            finish();
        }
    }
}

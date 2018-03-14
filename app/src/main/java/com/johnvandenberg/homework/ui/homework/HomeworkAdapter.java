package com.johnvandenberg.homework.ui.homework;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.data.model.Homework;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {

    private Cursor cursor;


    public HomeworkAdapter(Cursor cursor, HomeClickListener mHomeClickListener) {
        this.cursor = cursor;
        this.mHomeClickListener = mHomeClickListener;
    }

    final private HomeClickListener mHomeClickListener;

    public interface HomeClickListener{
        void homeOnLongClick (Homework homework);
        void homeOnClick (Homework homework);
    }



    @Override
    public HomeworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homework, parent, false);
        return new HomeworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeworkViewHolder holder, int position) {
        if (cursor != null) {
            // Move the cursor to the right position
            cursor.moveToPosition(position);
            // Create a homework object from the cursor's data
            // Bind the homework object to the view
            holder.bind(new Homework(cursor));
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {

        if (cursor != null) cursor.close();

        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    public class HomeworkViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private Homework homework;

        public HomeworkViewHolder(View itemView) {

            super(itemView);


            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }

        void bind(final Homework homework) {
            this.homework = homework;
            TextView titleView = itemView.findViewById(R.id.title);
            TextView dateView = itemView.findViewById(R.id.date);
            TextView subjectView = itemView.findViewById(R.id.subject);
            CheckBox checkBox = itemView.findViewById(R.id.checkbox);
            titleView.setText(homework.getTitle());
            dateView.setText(homework.getDate());
            subjectView.setText(homework.getSubject());
            checkBox.setChecked(homework.isFinished());
        }

        public Homework getHomework() {
            return homework;
        }

        @Override
        public boolean onLongClick(View view) {
            mHomeClickListener.homeOnLongClick(homework);
            return true;
        }

        @Override
        public void onClick(View view) {
            mHomeClickListener.homeOnClick(homework);

        }
    }
}
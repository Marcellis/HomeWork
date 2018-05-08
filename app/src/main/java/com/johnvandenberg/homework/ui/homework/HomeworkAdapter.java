package com.johnvandenberg.homework.ui.homework;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.johnvandenberg.homework.R;
import com.johnvandenberg.homework.database.entity.Homework;

public class HomeworkAdapter extends PagedListAdapter<Homework, HomeworkAdapter.HomeworkViewHolder> {

    private final HomeClickListener mHomeClickListener;

    HomeworkAdapter( HomeClickListener mHomeClickListener ) {
        super(DIFF_CALLBACK);
        this.mHomeClickListener = mHomeClickListener;
    }

    public interface HomeClickListener{
        void homeOnLongClick (Homework homework);
        void homeOnClick (Homework homework);
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);

        return new HomeworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder holder, int position) {
        Homework homework = getItem(position);

        if (homework != null) {
            holder.bindTo(homework);

        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();
        }
    }

    private static final DiffUtil.ItemCallback<Homework> DIFF_CALLBACK = new DiffUtil.ItemCallback<Homework>() {
        @Override
        public boolean areItemsTheSame(@NonNull Homework oldHomework, @NonNull Homework newHomework ) {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            return oldHomework.getUid() == newHomework.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Homework oldHomework, @NonNull Homework newHomework) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            return oldHomework.equals( newHomework );
        }
    };

    public class HomeworkViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        private Homework homework;

        HomeworkViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bindTo(final Homework homework) {
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

        void clear() {}

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
package com.laurum.Reminders;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laurum.R;
import com.laurum.Reminders.ReminderContent.ReminderItem;
import com.laurum.databinding.FragmentReminderBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ReminderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.ViewHolder> {

    private final List<ReminderItem> mValues;

    public ReminderRecyclerViewAdapter(List<ReminderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentReminderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), v.getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ReminderItem mItem;

        public ViewHolder(FragmentReminderBinding binding) {
            super(binding.getRoot());
            mIdView = binding.reminderTitle;
            mContentView = binding.reminderDesc;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
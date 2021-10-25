package com.laurum.Courses;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laurum.Faculty.Faculty;
import com.laurum.databinding.FragmentCoursesBinding;
import com.laurum.databinding.FragmentFacultyBinding;

import java.util.List;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;

    public CoursesRecyclerViewAdapter(List<Course> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentCoursesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.course_id.setText(mValues.get(position).getId());
        holder.course_title.setText(mValues.get(position).getTitle());
        holder.course_desc = mValues.get(position).getDesc();

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), holder.course_desc, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView course_id;
        public final TextView course_title;
        public String course_desc = "";
        public Course mItem;

        public ViewHolder(FragmentCoursesBinding binding) {
            super(binding.getRoot());
            course_id = binding.courseId;
            course_title = binding.courseTitle;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + course_title.getText()  + "'";
        }
    }
}
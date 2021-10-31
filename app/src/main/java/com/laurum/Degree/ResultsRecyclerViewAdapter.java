package com.laurum.Degree;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.laurum.Courses.Course;
import com.laurum.Database.LaurumDB;
import com.laurum.R;
import com.laurum.databinding.CourseInfoDialogBinding;
import com.laurum.databinding.DegreeCourseItemBinding;
import com.laurum.databinding.FragmentCoursesBinding;

import java.util.List;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private int lastPosition = -1;

    public ResultsRecyclerViewAdapter(List<Course> items) {
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

        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(v.getContext(), holder.course_desc, Toast.LENGTH_SHORT).show();
            //LaurumDB.addToDegree(holder.course_id.getText().toString());
            DegreeFragment.dismissDialog();
            DegreeFragment.addCourse(holder.course_id.getText().toString());
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

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
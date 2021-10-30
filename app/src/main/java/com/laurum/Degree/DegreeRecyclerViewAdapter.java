package com.laurum.Degree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.laurum.Courses.Course;
import com.laurum.R;
import com.laurum.databinding.DegreeCourseItemBinding;
import com.laurum.databinding.FragmentCoursesBinding;

import java.util.List;

public class DegreeRecyclerViewAdapter extends RecyclerView.Adapter<DegreeRecyclerViewAdapter.ViewHolder> {

    private final List<Course> mValues;
    private int lastPosition = -1;

    public DegreeRecyclerViewAdapter(List<Course> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(DegreeCourseItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.course_dialog, null);
            TextView textView = view.findViewById(R.id.courseDialog_id);
            textView.setText(mValues.get(position).getId());
            textView = view.findViewById(R.id.courseTitle);
            textView.setText(mValues.get(position).getTitle());
            textView = view.findViewById(R.id.courseCredits);
            textView.setText(mValues.get(position).getCredits().toString());
            textView = view.findViewById(R.id.courseDesc);
            textView.setText(mValues.get(position).getDesc());

            builder.setView(view);
            builder.setNegativeButton("Close", (dialog, id) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        ImageButton remove_button = holder.itemView.findViewById(R.id.course_remove);
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Course Remove Requested: " + mValues.get(holder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        CheckBox completed = holder.itemView.findViewById(R.id.checkbox_complete);
        completed.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked)
                Toast.makeText(compoundButton.getContext(), "Course Completed: " + mValues.get(holder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(compoundButton.getContext(), "Course Incompleted: " + mValues.get(holder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
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

        public ViewHolder(DegreeCourseItemBinding binding) {
            super(binding.getRoot());
            course_id = binding.degreeCourseId;
            course_title = binding.degreeCourseTitle;
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
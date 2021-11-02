package com.laurum;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laurum.databinding.DegreeCourseItemBinding;
import com.laurum.databinding.FragmentCoursesBinding;

import java.util.List;

public class DegreeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private static AlertDialog lastDialog;
    private static RecyclerView primary_RecyclerView;
    private static List<Course> courses;
    private static DegreeRecyclerViewAdapter course_adapter;
    private static ProgressBar degreeProgress;
    private static TextView degreeProgressPercent;

    public DegreeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DegreeFragment newInstance(int columnCount) {
        DegreeFragment fragment = new DegreeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_degree, container, false);

        Context context = view.getContext();
        primary_RecyclerView = view.findViewById(R.id.degree_course_list);
        if (mColumnCount <= 1)
            primary_RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            primary_RecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        courses = Database.LaurumDB.getDegreeCourses();
        course_adapter = new DegreeRecyclerViewAdapter(courses);
        primary_RecyclerView.setAdapter(course_adapter);

        degreeProgress = view.findViewById(R.id.progressBar);
        degreeProgressPercent = view.findViewById(R.id.degreeProgressPercent);
        //degreeProgress.setScaleY(5f);
        updateProgress();

        FloatingActionButton fab = view.findViewById(R.id.add_course_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View searchView = inflater.inflate(R.layout.course_search_dialog, null);
                builder.setView(searchView);
                builder.setPositiveButton("Search", (dialog, id) -> {
                    TextView search_input = searchView.findViewById(R.id.course_add_search_input);
                    if (search_input.getText().toString().length() > 0) {
                        dialog.dismiss();
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater2 = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View resultsView = inflater2.inflate(R.layout.search_results_dialog, null);
                        RecyclerView resultsRecycler = resultsView.findViewById(R.id.search_results_list);
                        List<Course> results = Database.LaurumDB.searchCourseList(search_input.getText().toString());
                        ResultsRecyclerViewAdapter adapter = new ResultsRecyclerViewAdapter(results);
                        resultsRecycler.setAdapter(adapter);

                        builder2.setView(resultsView);
                        lastDialog = builder2.create();
                        lastDialog.show();
                    }
                    else
                        dialog.dismiss();
                });
                builder.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    public static void dismissDialog() {
        if (lastDialog != null)
            lastDialog.dismiss();
    }

    public static RecyclerView getPrimaryRecyclerView(){
        return primary_RecyclerView;
    }

    private static boolean courseExists(String course_id){
        for (Course course : courses)
            if ((course.getId()).compareTo(course_id) == 0)
                return true;
        return false;
    }

    public static void addCourse(String course_id){

        if (courseExists(course_id))
            return;

        Database.LaurumDB.addToDegree(course_id);
        Course course = Database.LaurumDB.getCourse(course_id);
        courses.add(course);
        course_adapter.notifyItemInserted(course_adapter.getItemCount());
        updateProgress();
    }

    public static void removeCourse(int position){
        Database.LaurumDB.removeFromDegree(courses.get(position).getId());
        courses.remove(position);
        course_adapter.notifyItemRemoved(position);
        updateProgress();
    }

    public static void updateProgress(){
        int total_credits = 0;
        for (Course course : courses){
            if (course.getStatus() == 1)
                total_credits += 100*course.getCredits();
        }
        //degreeProgress.setProgress(total_credits);
        ObjectAnimator.ofInt(degreeProgress, "progress", total_credits)
                .setDuration(300)
                .start();
        degreeProgress.setProgress(total_credits);
        degreeProgressPercent.setText(String.format("%s%%", 100 * (double) degreeProgress.getProgress() / (double) degreeProgress.getMax()));
    }

    private static class DegreeRecyclerViewAdapter extends RecyclerView.Adapter<DegreeRecyclerViewAdapter.ViewHolder> {

        private final List<Course> mValues;
        private int lastPosition = -1;

        public DegreeRecyclerViewAdapter(List<Course> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public DegreeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(DegreeCourseItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final DegreeRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.course_id.setText(mValues.get(position).getId());
            holder.course_title.setText(mValues.get(position).getTitle());
            holder.course_desc = mValues.get(position).getDesc();
            holder.course_completed.setChecked(mValues.get(position).getStatus() == 1);

            setAnimation(holder.itemView, position);

            holder.itemView.setOnClickListener(v -> {
                //Toast.makeText(v.getContext(), holder.course_desc, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.course_info_dialog, null);
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
            remove_button.setOnClickListener(view -> {
                //Toast.makeText(view.getContext(), "Course Remove Requested: " + mValues.get(holder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();
                Database.LaurumDB.removeFromDegree(mValues.get(holder.getAdapterPosition()).getId());
                RecyclerView p_recycler = DegreeFragment.getPrimaryRecyclerView();
                DegreeFragment.removeCourse(holder.getAdapterPosition());
            });

            CheckBox completed = holder.itemView.findViewById(R.id.checkbox_complete);
            completed.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked)
                    holder.mItem.setStatus(1);
                else
                    holder.mItem.setStatus(0);
                updateProgress();
                Database.LaurumDB.degreeCourseStatusUpdate(holder.mItem.getId(), holder.mItem.getStatus());
            });

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView course_id;
            public final TextView course_title;
            public final CheckBox course_completed;
            public String course_desc = "";
            public Course mItem;

            public ViewHolder(DegreeCourseItemBinding binding) {
                super(binding.getRoot());
                course_id = binding.degreeCourseId;
                course_title = binding.degreeCourseTitle;
                course_completed = binding.checkboxComplete;
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

    private static class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

        private final List<Course> mValues;
        private int lastPosition = -1;

        public ResultsRecyclerViewAdapter(List<Course> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public ResultsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentCoursesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(final ResultsRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.course_id.setText(mValues.get(position).getId());
            holder.course_title.setText(mValues.get(position).getTitle());
            holder.course_desc = mValues.get(position).getDesc();

            setAnimation(holder.itemView, position);

            holder.itemView.setOnClickListener(v -> {
                //Toast.makeText(v.getContext(), holder.course_desc, Toast.LENGTH_SHORT).show();
                //LaurumDB.addToDegree(holder.course_id.getText().toString());
                dismissDialog();
                addCourse(holder.course_id.getText().toString());
            });

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
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
                return super.toString() + " '" + course_title.getText() + "'";
            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

}
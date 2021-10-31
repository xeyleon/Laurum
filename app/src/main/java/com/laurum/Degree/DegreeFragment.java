package com.laurum.Degree;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laurum.Courses.Course;
import com.laurum.Database.LaurumDB;
import com.laurum.R;

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

        courses = LaurumDB.getDegreeCourses();
        course_adapter = new DegreeRecyclerViewAdapter(courses);
        primary_RecyclerView.setAdapter(course_adapter);

        degreeProgress = view.findViewById(R.id.progressBar);
        TextView label = view.findViewById(R.id.degreeProgressPercent);
        label.setText(String.format("%s%%", 100 * (double) degreeProgress.getProgress() / (double) degreeProgress.getMax()));
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
                        List<Course> results = LaurumDB.searchCourseList(search_input.getText().toString());
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

    public static void addCourse(String course_id){
        LaurumDB.addToDegree(course_id);
        Course course = LaurumDB.getCourse(course_id);
        courses.add(course);
        course_adapter.notifyItemInserted(course_adapter.getItemCount());
        updateProgress();
    }

    public static void removeCourse(int position){
        LaurumDB.removeFromDegree(courses.get(position).getId());
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
        degreeProgress.setProgress(total_credits);
    }

}
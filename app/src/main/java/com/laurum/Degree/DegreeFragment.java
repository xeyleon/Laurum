package com.laurum.Degree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laurum.Courses.Course;
import com.laurum.Database.DatabaseHelper;
import com.laurum.Database.LaurumDB;
import com.laurum.R;

import java.util.List;

public class DegreeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

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
        RecyclerView recyclerView = view.findViewById(R.id.degree_course_list);
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        List<Course> courses = LaurumDB.getCourseList();
        DegreeRecyclerViewAdapter course_adapter = new DegreeRecyclerViewAdapter(courses);
        recyclerView.setAdapter(course_adapter);

        ProgressBar degreeProgress = view.findViewById(R.id.progressBar);
        TextView label = view.findViewById(R.id.degreeProgressPercent);
        label.setText(String.format("%s%%", 100 * (double) degreeProgress.getProgress() / (double) degreeProgress.getMax()));
        //degreeProgress.setScaleY(5f);

        return view;
    }

}
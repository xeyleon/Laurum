package com.laurum.Courses;

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
import android.widget.Toast;

import com.laurum.Database.DatabaseHelper;
import com.laurum.Database.LaurumDB;
import com.laurum.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public CoursesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CoursesFragment newInstance(int columnCount) {
        CoursesFragment fragment = new CoursesFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.courses_list);
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        List<Course> courses = LaurumDB.getCourseList();
        CoursesRecyclerViewAdapter course_adapter = new CoursesRecyclerViewAdapter(courses);
        recyclerView.setAdapter(course_adapter);

        EditText search_input = view.findViewById(R.id.course_search_input);
        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(getContext(), editable.toString(), Toast.LENGTH_SHORT).show();
                List<Course> courses = LaurumDB.searchCourseList(editable.toString());
                CoursesRecyclerViewAdapter course_adapter = new CoursesRecyclerViewAdapter(courses);
                recyclerView.setAdapter(course_adapter);
                //course_adapter.notifyDataSetChanged();
            }

        });

        return view;
    }

}
package com.laurum.Faculty;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.laurum.Courses.Course;
import com.laurum.Courses.CoursesRecyclerViewAdapter;
import com.laurum.Database.LaurumDB;
import com.laurum.R;

import java.util.List;

public class FacultyFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public FacultyFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FacultyFragment newInstance(int columnCount) {
        FacultyFragment fragment = new FacultyFragment();
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
        View view = inflater.inflate(R.layout.fragment_faculty_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.faculty_list);
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        List<Faculty> faculty = LaurumDB.getFacultyList();
        FacultyRecyclerViewAdapter faculty_adapter = new FacultyRecyclerViewAdapter(faculty);
        recyclerView.setAdapter(faculty_adapter);

        EditText search_input = view.findViewById(R.id.faculty_search_input);
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
                List<Faculty> faculty = LaurumDB.searchFacultyList(editable.toString());
                FacultyRecyclerViewAdapter faculty_adapter = new FacultyRecyclerViewAdapter(faculty);
                recyclerView.setAdapter(faculty_adapter);
                //course_adapter.notifyDataSetChanged();
            }

        });

        return view;
    }

}
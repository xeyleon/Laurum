package com.laurum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.laurum.databinding.FragmentCoursesBinding;

import java.util.List;

public class CoursesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    List<Course> courses;

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
        recyclerView = view.findViewById(R.id.courses_list);
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        courses = Database.LaurumDB.getCourseList();
        CoursesRecyclerViewAdapter course_adapter = new CoursesRecyclerViewAdapter(courses);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setAdapter(course_adapter);

        EditText search_input = view.findViewById(R.id.course_search_input);
        search_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(getContext(), editable.toString(), Toast.LENGTH_SHORT).show();
                //courses = LaurumDB.searchCourseList(editable.toString());
                //CoursesRecyclerViewAdapter course_adapter = new CoursesRecyclerViewAdapter(courses);
                //recyclerView.setAdapter(course_adapter);
                courses.clear();
                courses.addAll(Database.LaurumDB.searchCourseList(editable.toString()));
                course_adapter.notifyDataSetChanged();
            }

        });

        return view;
    }

    private static class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.ViewHolder> {

        private final List<Course> mValues;
        private int lastPosition = -1;

        public CoursesRecyclerViewAdapter(List<Course> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public CoursesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentCoursesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final CoursesRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.course_id.setText(mValues.get(position).getId());
            holder.course_title.setText(mValues.get(position).getTitle());
            holder.course_desc = mValues.get(position).getDesc();

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

}
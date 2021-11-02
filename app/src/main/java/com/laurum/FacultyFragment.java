package com.laurum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.laurum.databinding.FragmentFacultyBinding;

import java.util.List;

public class FacultyFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    List<Faculty> faculty;

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

        faculty = Database.LaurumDB.getFacultyList();
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

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(getContext(), editable.toString(), Toast.LENGTH_SHORT).show();
                //List<Faculty> faculty = LaurumDB.searchFacultyList(editable.toString());
                //FacultyRecyclerViewAdapter faculty_adapter = new FacultyRecyclerViewAdapter(faculty);
                //recyclerView.setAdapter(faculty_adapter);
                faculty.clear();
                faculty.addAll(Database.LaurumDB.searchFacultyList(editable.toString()));
                faculty_adapter.notifyDataSetChanged();
            }

        });

        return view;
    }

    private static class FacultyRecyclerViewAdapter extends RecyclerView.Adapter<FacultyRecyclerViewAdapter.ViewHolder> {

        private final List<Faculty> mValues;
        private int lastPosition = -1;
        public FacultyRecyclerViewAdapter(List<Faculty> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public FacultyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(FragmentFacultyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(final FacultyRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.name.setText(mValues.get(position).getName());
            holder.email = mValues.get(position).getEmail();
            holder.title = mValues.get(position).getTitle();
            holder.department = mValues.get(position).getDepartment();

            setAnimation(holder.itemView, position);

            holder.itemView.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.faculty_dialog, null);
                TextView textView = view.findViewById(R.id.facultyName);
                textView.setText(mValues.get(position).getName());
                textView = view.findViewById(R.id.facultyTitle);
                textView.setText(mValues.get(position).getTitle());
                textView = view.findViewById(R.id.facultyDialog_title);
                textView.setText(mValues.get(position).getName());
                textView = view.findViewById(R.id.facultyDep);
                textView.setText(mValues.get(position).getDepartment());
                textView = view.findViewById(R.id.facultyEmail);
                textView.setText(mValues.get(position).getEmail());

                builder.setView(view);
                builder.setPositiveButton("Email", (dialog, id) -> {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{holder.email});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    email.putExtra(Intent.EXTRA_TEXT, "");
                    email.setType("message/rfc822");
                    v.getContext().startActivity(email);
                });
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
            public final TextView name;
            public String email = "";
            public String title = "";
            public String department = "";
            public Faculty mItem;

            public ViewHolder(FragmentFacultyBinding binding) {
                super(binding.getRoot());
                name = binding.facultyName;
            }

            @NonNull
            @Override
            public String toString() {
                return super.toString() + " '" + name.getText()  + "'";
            }
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }
}
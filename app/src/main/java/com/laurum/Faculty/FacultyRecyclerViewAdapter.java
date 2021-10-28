package com.laurum.Faculty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.laurum.R;
import com.laurum.databinding.FragmentFacultyBinding;

import java.util.List;

public class FacultyRecyclerViewAdapter extends RecyclerView.Adapter<FacultyRecyclerViewAdapter.ViewHolder> {

    private final List<Faculty> mValues;
    private int lastPosition = -1;
    public FacultyRecyclerViewAdapter(List<Faculty> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentFacultyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(mValues.get(position).getName());
        holder.email = mValues.get(position).getEmail();
        holder.title = mValues.get(position).getTitle();
        holder.department = mValues.get(position).getDepartment();

        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), holder.resTitle.getText().toString(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Intent.ACTION_VIEW, holder.resUri);
//            v.getContext().startActivity(intent);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.faculty_dialog, null);
            TextView textView = (TextView)view.findViewById(R.id.facultyName);
            textView.setText(mValues.get(position).getName());
            textView = (TextView)view.findViewById(R.id.facultyTitle);
            textView.setText(mValues.get(position).getTitle());
            textView = (TextView)view.findViewById(R.id.facultyDialog_title);
            textView.setText(mValues.get(position).getName());
            textView = (TextView)view.findViewById(R.id.facultyDep);
            textView.setText(mValues.get(position).getDepartment());
            textView = (TextView)view.findViewById(R.id.facultyEmail);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
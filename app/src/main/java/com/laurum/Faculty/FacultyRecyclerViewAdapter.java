package com.laurum.Faculty;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.name.setText(String.format("%s, %s", mValues.get(position).getLastName(),mValues.get(position).getFirstName()));
        holder.email.setText(mValues.get(position).getEmail());

        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), holder.resTitle.getText().toString(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Intent.ACTION_VIEW, holder.resUri);
//            v.getContext().startActivity(intent);

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ holder.email.getText().toString()});
            email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            email.putExtra(Intent.EXTRA_TEXT, "");
            email.setType("message/rfc822");
            v.getContext().startActivity(email);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView email;
        public Faculty mItem;

        public ViewHolder(FragmentFacultyBinding binding) {
            super(binding.getRoot());
            name = binding.facultyName;
            email = binding.facultyEmail;
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
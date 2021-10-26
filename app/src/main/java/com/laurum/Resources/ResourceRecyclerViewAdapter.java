package com.laurum.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.laurum.databinding.FragmentResourceBinding;

import java.util.List;

public class ResourceRecyclerViewAdapter extends RecyclerView.Adapter<ResourceRecyclerViewAdapter.ViewHolder> {

    private final List<Resource> mValues;
    private Context context;
    private int lastPosition = -1;

    public ResourceRecyclerViewAdapter(List<Resource> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(FragmentResourceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.resTitle.setText(mValues.get(position).getTitle());
        holder.resDesc.setText(mValues.get(position).getDesc());
        holder.resUri = Uri.parse(mValues.get(position).getUrl());
        holder.resIcon.setImageResource(context.getResources().getIdentifier(mValues.get(position).getIcon(), "drawable", context.getPackageName()));

        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), holder.resTitle.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_VIEW, holder.resUri);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView resTitle;
        public final TextView resDesc;
        public final ImageView resIcon;
        public Uri resUri;
        public Resource mItem;

        public ViewHolder(FragmentResourceBinding binding) {
            super(binding.getRoot());
            resTitle = binding.resourceTitle;
            resDesc = binding.resourceDesc;
            resIcon = binding.resourceIcon;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + resDesc.getText() + "'";
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
package com.laurum.Resources;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.laurum.R;
import com.laurum.Resources.ResourceContent.ResourceItem;
import com.laurum.databinding.FragmentResourceBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ResourceItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ResourceRecyclerViewAdapter extends RecyclerView.Adapter<ResourceRecyclerViewAdapter.ViewHolder> {

    private final List<ResourceItem> mValues;

    public ResourceRecyclerViewAdapter(List<ResourceItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentResourceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.resTitle.setText(mValues.get(position).title);
        holder.resDesc.setText(mValues.get(position).description);
        //holder.resIcon.setImageResource(R.drawable.ic_courses_tab);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), holder.resTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, holder.resUri);
                v.getContext().startActivity(intent);
            }
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
        public final Uri resUri = Uri.parse("https://google.com");
        public ResourceItem mItem;

        public ViewHolder(FragmentResourceBinding binding) {
            super(binding.getRoot());
            resTitle = binding.resourceTitle;
            resDesc = binding.resourceDesc;
            resIcon = binding.resourceIcon;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + resDesc.getText() + "'";
        }
    }
}
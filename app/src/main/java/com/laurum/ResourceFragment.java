package com.laurum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ResourceFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public ResourceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ResourceFragment newInstance(int columnCount) {
        ResourceFragment fragment = new ResourceFragment();
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
        View view = inflater.inflate(R.layout.fragment_resource_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            List<Resource> resources = Database.LaurumDB.getResourceList();

            recyclerView.setAdapter(new ResourceRecyclerViewAdapter(resources));
        }

        return view;
    }

    private static class ResourceRecyclerViewAdapter extends RecyclerView.Adapter<ResourceRecyclerViewAdapter.ViewHolder> {

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
            return new ResourceRecyclerViewAdapter.ViewHolder(FragmentResourceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(final ResourceRecyclerViewAdapter.ViewHolder holder, int position) {
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

        public static class ViewHolder extends RecyclerView.ViewHolder {
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
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

}
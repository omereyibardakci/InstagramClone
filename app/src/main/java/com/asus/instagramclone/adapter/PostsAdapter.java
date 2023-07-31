package com.asus.instagramclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asus.instagramclone.databinding.ReceylerRowBinding;
import com.asus.instagramclone.model.Posts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    ArrayList<Posts> postsArrayList = new ArrayList<Posts>();

    public PostsAdapter(ArrayList<Posts> postsArrayList) {
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReceylerRowBinding receylerRowBinding = ReceylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostsHolder(receylerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        holder.binging.receylerViewUserEmailText.setText(postsArrayList.get(position).userEmail);
        holder.binging.receylerViewCommentText.setText(postsArrayList.get(position).comment);
        Picasso.get().load(postsArrayList.get(position).downloadUrl).into(holder.binging.receylerViewImageView);        // I used picasso library for view pictures

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public class PostsHolder extends RecyclerView.ViewHolder {

        private ReceylerRowBinding binging;

        public PostsHolder(@NonNull ReceylerRowBinding binding) {
            super(binding.getRoot());
            this.binging=binding;
        }
    }


}

package com.adiupd123.fingertrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFeedRVAdapter extends RecyclerView.Adapter<HomeFeedRVAdapter.HomeFeedViewHolder> {
    private ArrayList<HashMap<String, Object>> posts;
    private Context context;

    public HomeFeedRVAdapter(ArrayList<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }


    @NonNull
    @Override
    public HomeFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFeedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class HomeFeedViewHolder extends RecyclerView.ViewHolder{

        public HomeFeedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

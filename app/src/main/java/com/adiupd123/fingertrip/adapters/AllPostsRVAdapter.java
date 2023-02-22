package com.adiupd123.fingertrip.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.fragments.PostFragment;
import com.adiupd123.fingertrip.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllPostsRVAdapter extends RecyclerView.Adapter<AllPostsRVAdapter.PostViewHolder> {
    List<HashMap<String, Object>> posts;
    Context context;
    FirebaseAuth  mAuth;
    public AllPostsRVAdapter(List<HashMap<String, Object>> posts, Context context) {
        this.posts = posts;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        Collections.sort(posts, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String time1 = o1.get("postTimeStamp").toString();
                String time2 = o2.get("postTimeStamp").toString();
                DateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");
                try {
                    Date date1 = format.parse(time1);
                    Date date2 = format.parse(time2);
                    return date2.compareTo(date1); // sort in descending order
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_user_profile_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        HashMap<String, Object> post = posts.get(position);
        Glide.with(holder.imageView.getContext())
                .load(post.get("postPhoto").toString())
                .placeholder(R.drawable.default_post_image)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PostFragment fragment = new PostFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);
                bundle.putString("emailID", mAuth.getCurrentUser().getEmail());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container_view, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postImage);
        }
    }
}

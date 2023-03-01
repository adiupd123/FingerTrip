package com.adiupd123.fingertrip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSearchRVAdapter extends RecyclerView.Adapter<UserSearchRVAdapter.SearchedUserViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, Object>> searchedUsers;

    public UserSearchRVAdapter(Context context, ArrayList<HashMap<String, Object>> searchedUsers) {
        this.context = context;
        this.searchedUsers = searchedUsers;
    }

    @NonNull
    @Override
    public SearchedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_user_layout, parent, false);
        return new SearchedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedUserViewHolder holder, int position) {
        HashMap<String, Object> personalInfo, socialInfo;
        personalInfo = (HashMap<String, Object>) searchedUsers.get(position).get("personal_info");
        socialInfo = (HashMap<String, Object>) searchedUsers.get(position).get("social_info");
        Glide.with(context)
                .load(socialInfo.get("profilePhoto"))
                .placeholder(R.drawable.ic_user_profile)
                .into(holder.userProfileIV);
        holder.usernameTV.setText("@" + personalInfo.get("username").toString());
        holder.nameTV.setText(personalInfo.get("name").toString());
    }

    @Override
    public int getItemCount() {
        return searchedUsers.size();
    }

    public class SearchedUserViewHolder extends RecyclerView.ViewHolder{
        private ImageView userProfileIV;
        private TextView usernameTV, nameTV;
        public SearchedUserViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileIV = itemView.findViewById(R.id.searchedUser_profile_imageView);
            usernameTV = itemView.findViewById(R.id.searchedUser_username_textView);
            nameTV = itemView.findViewById(R.id.searchedUser_name_textView);
        }
    }
}

package com.adiupd123.fingertrip.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.R;
import com.adiupd123.fingertrip.fragments.UserFragment;
import com.adiupd123.fingertrip.fragments.UserProfileFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersRVAdapter extends RecyclerView.Adapter<UsersRVAdapter.SearchedUserViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, Object>> searchedUsers;
    private FirebaseAuth mAuth;
    private HashMap<String, Object> personalInfo, socialInfo;

    public UsersRVAdapter(Context context, ArrayList<HashMap<String, Object>> searchedUsers) {
        this.context = context;
        this.searchedUsers = searchedUsers;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public SearchedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new SearchedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedUserViewHolder holder, int position) {
        personalInfo = (HashMap<String, Object>) searchedUsers.get(position).get("personal_info");
        socialInfo = (HashMap<String, Object>) searchedUsers.get(position).get("social_info");
        Glide.with(context)
                .load(socialInfo.get("profilePhoto"))
                .placeholder(R.drawable.ic_user_profile)
                .into(holder.userProfileIV);
        holder.usernameTV.setText("@" + personalInfo.get("username").toString());
        holder.nameTV.setText(personalInfo.get("name").toString());
        holder.itemView.setOnClickListener(
                click ->
                        openUserProfile(
                                personalInfo.get("emailID").toString().replace('.',','),
                                mAuth.getCurrentUser().getEmail()
                                ));
    }
    private void openUserProfile(String userEmail, String curUserEmail) {
        Bundle bundle = new Bundle();
        if(!userEmail.equals(curUserEmail.replace('.',','))){
            UserFragment userFragment = new UserFragment();
            bundle.putString("emailID", curUserEmail);
            bundle.putString("ownerID", userEmail);
            bundle.putSerializable("personalInfo", personalInfo);
            bundle.putSerializable("socialInfo", socialInfo);
            userFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, userFragment)
                    .addToBackStack(null)
                    .commit();
        } else{
            bundle.putString("emailID", userEmail);
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            userProfileFragment.setArguments(bundle);
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, userProfileFragment)
                    .addToBackStack(null)
                    .commit();
        }
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

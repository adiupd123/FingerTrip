package com.adiupd123.fingertrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adiupd123.fingertrip.models.CommentModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentsRVAdapter extends RecyclerView.Adapter<CommentsRVAdapter.CommentsViewHolder> {

    private ArrayList<CommentModel> comments;
    private Context context;

    public CommentsRVAdapter(ArrayList<CommentModel> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        CommentModel commentModel = comments.get(position);
        Glide.with(context)
                .load(commentModel.getProfilePhoto())
                .into(holder.profileIV);
        holder.usernameTV.setText("@"+commentModel.getUsername());
        holder.timeStampTV.setText(commentModel.getCommentTimeStamp());
        holder.commentTV.setText(commentModel.getComment());
        holder.profileIV.setOnClickListener(click -> {
            Toast.makeText(context, "Commenter's profile", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        private ImageView profileIV;
        private TextView usernameTV, commentTV, timeStampTV;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIV = itemView.findViewById(R.id.commenterProfile_imageView);
            usernameTV = itemView.findViewById(R.id.commenterUsername_textView);
            commentTV = itemView.findViewById(R.id.postComment_textView);
            timeStampTV = itemView.findViewById(R.id.commentTime_textView);
        }
    }
}

package com.hfad.nationalparksguide.Controller.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hfad.nationalparksguide.R;
import com.hfad.nationalparksguide.data.model.Comment;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<Comment> mData;

    public CommentListAdapter(List<Comment> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         Comment comment = mData.get(position);
        holder.tvUserName.setText(comment.getUserName());
        holder.tvComment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvComment;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }
}


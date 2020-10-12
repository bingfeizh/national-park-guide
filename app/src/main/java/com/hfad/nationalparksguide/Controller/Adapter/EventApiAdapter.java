package com.hfad.nationalparksguide.Controller.Adapter;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.nationalparksguide.R;
import com.hfad.nationalparksguide.Room.ParkInfo;
import com.hfad.nationalparksguide.data.model.Event;
import com.hfad.nationalparksguide.data.model.Park;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EventApiAdapter extends RecyclerView.Adapter<com.hfad.nationalparksguide.Controller.Adapter.EventApiAdapter.ViewHolder> {
    private List<Event> mData;

    public EventApiAdapter(List<Event> data) {
        this.mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    @Override
    public com.hfad.nationalparksguide.Controller.Adapter.EventApiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        return new com.hfad.nationalparksguide.Controller.Adapter.EventApiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.hfad.nationalparksguide.Controller.Adapter.EventApiAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        if (title.length() == 0) {
            title = "No Events";
        }
        holder.tvTitle.setText(title);

        String description = mData.get(position).getDescription();
        String normalDes = Html.fromHtml(description).toString();
        holder.tvDescription.setText(normalDes);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

package com.hfad.nationalparksguide.Controller.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.nationalparksguide.R;
import com.hfad.nationalparksguide.Room.ParkInfo;

import java.util.List;


public class ParkListAdapter extends RecyclerView.Adapter<ParkListAdapter.ViewHolder> {
    private List<ParkInfo> mData;
    private OnParkClickListenr mOnParkClickListenr;

    public ParkListAdapter(List<ParkInfo> data, OnParkClickListenr onParkClickListenr) {
        this.mData = data;
        this.mOnParkClickListenr = onParkClickListenr;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivImage;
        TextView tvName;
        TextView tvDescription;

        OnParkClickListenr onParkClickListenr;


        ViewHolder(View itemView, OnParkClickListenr onParkClickListenr) {
            super(itemView);
            //ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            this.onParkClickListenr = onParkClickListenr;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onParkClickListenr.onParkClick(getAdapterPosition());
        }
    }

    public interface OnParkClickListenr{
        void onParkClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_row, parent, false);
        return new ViewHolder(view, mOnParkClickListenr);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mData.get(position).parkName;
        holder.tvName.setText(name);


//        String poster = mData.get(position).getImages().get(0).getURL();
//        String posterPath = poster;
//        Picasso.get().load(posterPath).into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}


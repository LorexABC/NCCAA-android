package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Utils.UserSession;
import com.bumptech.glide.Glide;
import com.app_nccaa.nccaa.R;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;

    private String[] titleHome;
    private int[] imagesHome;

    private UserSession session;


    public AdapterHome(Context context, String[] titleHome, int[] imagesHome, OnItemClickListener mListener) {
        this.context = context;
        this.titleHome = titleHome;
        this.imagesHome = imagesHome;
        this.mListener = mListener;
        session = new UserSession(context);
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_home, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });


        if (position == 11) {
            holder.titleTV.setText(Html.fromHtml(titleHome[position]));
        } else {
            holder.titleTV.setText(titleHome[position]);
        }

        Glide.with(context).load(imagesHome[position]).into(holder.homeImage);

    }

    @Override
    public int getItemCount() {
        return titleHome.length;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView titleTV;
        ImageView homeImage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            homeImage = itemView.findViewById(R.id.homeImage);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }


}

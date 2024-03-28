package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.AnnouncementsModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterAnnouncement extends RecyclerView.Adapter<AdapterAnnouncement.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<AnnouncementsModel> announcementsModels;


    public AdapterAnnouncement(Context context, ArrayList<AnnouncementsModel> announcementsModels, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.announcementsModels = announcementsModels;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_announcement, parent, false);
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

        holder.dateTV.setText(announcementsModels.get(position).getDate());
        holder.headerTV.setText(announcementsModels.get(position).getSubject());
        holder.decTV.setText(noTrailingwhiteLines(Html.fromHtml(announcementsModels.get(position).getText())));

    }

    @Override
    public int getItemCount() {
        return announcementsModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView decTV, headerTV, dateTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            decTV = itemView.findViewById(R.id.decTV);
            headerTV = itemView.findViewById(R.id.headerTV);
            dateTV = itemView.findViewById(R.id.dateTV);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }



    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }



}

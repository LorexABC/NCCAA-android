package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CME_HistoryModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterCreditHistory extends RecyclerView.Adapter<AdapterCreditHistory.Viewholder> {

    private final Context context;
    private final OnItemClickListener mListener;
    private final ArrayList<CME_HistoryModel> historyArrayList;
    private final String isCurrent;


    public AdapterCreditHistory(Context context, ArrayList<CME_HistoryModel> historyArrayList, String isCurrent, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.historyArrayList = historyArrayList;
        this.isCurrent = isCurrent;

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        if (isCurrent.equals("true")){
            view = inflater.inflate(R.layout.adapter_history_blue, parent, false);
        } else {
            view = inflater.inflate(R.layout.adapter_history_orng, parent, false);
        }
        return new Viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        if (position +1 == historyArrayList.size()){
            holder.lineBottom.setVisibility(View.GONE);
        }

        holder.updateEntry.setOnClickListener(view -> mListener.onItemUpdate(position));

        holder.deleteEntry.setOnClickListener(view -> mListener.onItemDelete(position));

        holder.viewTV.setOnClickListener(view -> mListener.onItemView(position));


        holder.description.setText(historyArrayList.get(position).getName());
        holder.credits.setText(historyArrayList.get(position).getHours());
        holder.uploadDate.setText(historyArrayList.get(position).getDateSubmitted());
        holder.type.setText(historyArrayList.get(position).getType().substring(0, 1).toUpperCase() + historyArrayList.get(position).getType().substring(1).toLowerCase());


    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {

        ImageView updateEntry, deleteEntry;
        TextView description, uploadDate, credits, type, viewTV;
        private final RelativeLayout lineBottom;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            updateEntry = itemView.findViewById(R.id.updateEntry);
            deleteEntry = itemView.findViewById(R.id.deleteEntry);
            description = itemView.findViewById(R.id.description);
            uploadDate = itemView.findViewById(R.id.uploadDate);
            credits = itemView.findViewById(R.id.credits);
            type = itemView.findViewById(R.id.type);
            viewTV = itemView.findViewById(R.id.viewTV);
            lineBottom = itemView.findViewById(R.id.lineBottom);

        }
    }

    public interface OnItemClickListener {
        void onItemUpdate(int pos);
        void onItemDelete(int pos);
        void onItemView(int pos);
    }
}

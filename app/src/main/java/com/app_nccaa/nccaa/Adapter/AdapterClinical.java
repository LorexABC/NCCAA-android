package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CasesModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterClinical extends RecyclerView.Adapter<AdapterClinical.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<CasesModel> casesModelArrayList;


    public AdapterClinical(Context context, ArrayList<CasesModel> casesModelArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.casesModelArrayList = casesModelArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_clinical, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {



        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemEdit(position);
            }
        });

        holder.tv_date.setText(casesModelArrayList.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return casesModelArrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView edit_btn;
        TextView tv_date;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            edit_btn = itemView.findViewById(R.id.edit_btn);
            tv_date = itemView.findViewById(R.id.tv_date);

        }
    }

    public interface OnItemClickListener {
        void onItemEdit(int pos);
    }

    public void updateList(ArrayList<CasesModel> list){
        this.casesModelArrayList = list;
        notifyDataSetChanged();
    }


}

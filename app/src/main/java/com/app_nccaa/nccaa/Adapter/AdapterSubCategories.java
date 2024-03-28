package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.SubcategoriesModel;
import com.app_nccaa.nccaa.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class AdapterSubCategories extends RecyclerView.Adapter<AdapterSubCategories.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;

    private ArrayList<SubcategoriesModel> subcatArrayList;


    public AdapterSubCategories(Context context, ArrayList<SubcategoriesModel> subcatArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.subcatArrayList = subcatArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_sub_categories, parent, false);


        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.cat_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemPopup(position);
            }
        });


        holder.cat_cb_tv.setText(subcatArrayList.get(position).getName());

        if (subcatArrayList.get(position).getDisplay_completed().equals("true") && subcatArrayList.get(position).getDisplay_target().equals("true")) {
            holder.cat_count_tv.setText(subcatArrayList.get(position).getFill() + "/" + subcatArrayList.get(position).getTarget());
        } else if (subcatArrayList.get(position).getDisplay_completed().equals("true") && subcatArrayList.get(position).getDisplay_target().equals("false")){
            holder.cat_count_tv.setText(subcatArrayList.get(position).getFill());
        } else if (subcatArrayList.get(position).getDisplay_completed().equals("false") && subcatArrayList.get(position).getDisplay_target().equals("true")){
            holder.cat_count_tv.setText(subcatArrayList.get(position).getTarget());
        } else {
            holder.cat_count_tv.setVisibility(View.INVISIBLE);
        }


        if (subcatArrayList.get(position).isChecked()){
            holder.cat_cb.setChecked(true);
        } else {
            holder.cat_cb.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return subcatArrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView cat_cb_tv, cat_count_tv;
        MaterialCheckBox cat_cb;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            cat_cb = itemView.findViewById(R.id.cat_cb);
            cat_cb_tv = itemView.findViewById(R.id.cat_cb_tv);
            cat_count_tv = itemView.findViewById(R.id.cat_count_tv);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
        void onItemPopup(int pos);
    }


}

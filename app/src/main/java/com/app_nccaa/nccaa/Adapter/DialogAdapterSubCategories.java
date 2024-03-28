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

public class DialogAdapterSubCategories extends RecyclerView.Adapter<DialogAdapterSubCategories.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;

    private ArrayList<SubcategoriesModel> subcatArrayList;
    private String tabPos;


    public DialogAdapterSubCategories(Context context, ArrayList<SubcategoriesModel> subcatArrayList, String tabPos, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.tabPos = tabPos;
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


      /*  holder.cat_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });
*/

        holder.cat_cb_tv.setText(subcatArrayList.get(position).getName());
  //      holder.cat_count_tv.setText(subcatArrayList.get(position).getFill() + "/" + subcatArrayList.get(position).getTarget());


        if (tabPos.equals("0")) {
            holder.cat_count_tv.setText(subcatArrayList.get(position).getFill() + "/" + subcatArrayList.get(position).getTarget());
        } else {
            int remain  = Integer.parseInt(subcatArrayList.get(position).getTarget()) - Integer.parseInt(subcatArrayList.get(position).getFill());
            holder.cat_count_tv.setText(remain + "/" + subcatArrayList.get(position).getTarget());
        }


       /* if (subcatArrayList.get(position).isChecked()){
            holder.cat_cb.setChecked(true);
        } else {
            holder.cat_cb.setChecked(false);
        }*/

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
    }


}

package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CategoriesModel;
import com.app_nccaa.nccaa.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class DialogAdapterCategories extends RecyclerView.Adapter<DialogAdapterCategories.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;

    private DialogAdapterSubCategories adapterSubCategories;
    private ArrayList<CategoriesModel> categoriesAllArrayList;

    private String tabPos;


    public DialogAdapterCategories(Context context, ArrayList<CategoriesModel> categoriesAllArrayList, String tabPos, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.tabPos = tabPos;
        this.categoriesAllArrayList = categoriesAllArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_categories, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


      /*  holder.cat_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });*/


        holder.sub_cat_rv.setLayoutManager(new LinearLayoutManager(context));
        adapterSubCategories = new DialogAdapterSubCategories(context, categoriesAllArrayList.get(position).getSubcatArrayList(), tabPos, new DialogAdapterSubCategories.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

              /*  if (categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).isChecked()){
                    categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(false);
                } else {
                    categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(true);
                }*/


            }
        });
        holder.sub_cat_rv.setAdapter(adapterSubCategories);

        holder.cat_cb_tv.setText(categoriesAllArrayList.get(position).getName());

        if (tabPos.equals("0")) {
            holder.cat_count_tv.setText(categoriesAllArrayList.get(position).getFill() + "/" + categoriesAllArrayList.get(position).getTarget());
        } else {
            int remain  = Integer.parseInt(categoriesAllArrayList.get(position).getTarget()) - Integer.parseInt(categoriesAllArrayList.get(position).getFill());
            holder.cat_count_tv.setText(remain + "/" + categoriesAllArrayList.get(position).getTarget());
        }

       /* if (categoriesAllArrayList.get(position).isChecked()){
            holder.cat_cb.setChecked(true);
        } else {
            holder.cat_cb.setChecked(false);
        }
*/

    }

    @Override
    public int getItemCount() {
        return categoriesAllArrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView cat_cb_tv, cat_count_tv;
        RecyclerView sub_cat_rv;
        MaterialCheckBox cat_cb;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            sub_cat_rv = itemView.findViewById(R.id.sub_cat_rv);
            cat_cb = itemView.findViewById(R.id.cat_cb);
            cat_cb_tv = itemView.findViewById(R.id.cat_cb_tv);
            cat_count_tv = itemView.findViewById(R.id.cat_count_tv);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void updateList(ArrayList<CategoriesModel> list){
        this.categoriesAllArrayList = list;
        notifyDataSetChanged();
    }


}

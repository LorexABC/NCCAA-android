package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Activity.CategoriesActivity;
import com.app_nccaa.nccaa.Model.CategoriesModel;
import com.app_nccaa.nccaa.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;

    private AdapterSubCategories adapterSubCategories;

    private ArrayList<CategoriesModel> categoriesAllArrayList;


    public AdapterCategories(Context context, ArrayList<CategoriesModel> categoriesAllArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
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


        holder.sub_cat_rv.setLayoutManager(new LinearLayoutManager(context));
        adapterSubCategories = new AdapterSubCategories(context, categoriesAllArrayList.get(position).getSubcatArrayList(), new AdapterSubCategories.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                int count = 0;

                if (categoriesAllArrayList.get(position).getSelect_multiple().equals("false")){

                    for (int i = 0; i < categoriesAllArrayList.get(position).getSubcatArrayList().size(); i++){
                        if (categoriesAllArrayList.get(position).getSubcatArrayList().get(i).isChecked()){
                            count++;
                        }
                    }

                    if (count <= 0){
                        categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(true);
                    } else {
                        categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(false);
                        Toast.makeText(context, "You can't select multiple sub-categories", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).isChecked()){
                        categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(false);
                    } else {
                        categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).setChecked(true);
                    }
                }

                notifyDataSetChanged();

            }

            @Override
            public void onItemPopup(int pos) {
                openDialog(categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).getName(),
                        categoriesAllArrayList.get(position).getSubcatArrayList().get(pos).getDescription());
            }
        });
        holder.sub_cat_rv.setAdapter(adapterSubCategories);

        holder.cat_cb_tv.setText(categoriesAllArrayList.get(position).getName());

        if (categoriesAllArrayList.get(position).getDisplay_completed().equals("true") && categoriesAllArrayList.get(position).getDisplay_target().equals("true")) {
            holder.cat_count_tv.setText(categoriesAllArrayList.get(position).getFill() + "/" + categoriesAllArrayList.get(position).getTarget());
        } else if (categoriesAllArrayList.get(position).getDisplay_completed().equals("true") && categoriesAllArrayList.get(position).getDisplay_target().equals("false")){
            holder.cat_count_tv.setText(categoriesAllArrayList.get(position).getFill());
        } else if (categoriesAllArrayList.get(position).getDisplay_completed().equals("false") && categoriesAllArrayList.get(position).getDisplay_target().equals("true")){
            holder.cat_count_tv.setText(categoriesAllArrayList.get(position).getTarget());
        } else {
            holder.cat_count_tv.setVisibility(View.INVISIBLE);
        }


        if (categoriesAllArrayList.get(position).isChecked()){
            holder.cat_cb.setChecked(true);
        } else {
            holder.cat_cb.setChecked(false);
        }

        if (categoriesAllArrayList.get(position).getIs_selectable().equals("true")){
            holder.cat_cb.setVisibility(View.VISIBLE);
        } else {
            holder.cat_cb.setVisibility(View.INVISIBLE);
        }


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
        void onItemPopup(int pos);
    }

    public void updateList(ArrayList<CategoriesModel> list){
        this.categoriesAllArrayList = list;
        notifyDataSetChanged();
    }


    private void openDialog(String name, String description) {

        Dialog dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.categories_info_dailog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(true);

        TextView catName = dialog1.findViewById(R.id.catName);
        TextView desName = dialog1.findViewById(R.id.desName);

        catName.setText(name);

        if (!description.equals("null")) {
            desName.setText(description);
        } else {
            desName.setVisibility(View.GONE);
        }

        dialog1.findViewById(R.id.closeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

}

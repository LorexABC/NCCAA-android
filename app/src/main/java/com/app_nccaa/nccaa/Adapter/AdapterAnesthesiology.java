package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterAnesthesiology extends RecyclerView.Adapter<AdapterAnesthesiology.Viewholder> {

    private final OnItemClickListener listener;
    private Context mContext;
    private ArrayList<CityModel> subCategoryModelArrayList;


    public AdapterAnesthesiology(Context mContext, ArrayList<CityModel> subCategoryModelArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.listener = onItemClickListener;
        this.subCategoryModelArrayList = subCategoryModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.adapter_add_another_city, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.cityName.setText(subCategoryModelArrayList.get(position).getName());

     /*   for (int i = 0; i<arrayCityId.size(); i++){
            String city = arrayCityId.get(i);

            if (city.equals(mDataCity.get(position).getCityId())){
                mDataCity.get(position).setChecked(true);
            }else {
                mDataCity.get(position).setChecked(false);
            }

        }*/


        if (subCategoryModelArrayList.get(position).isChecked()){
            holder.checkBoxAddCity.setChecked(true);
        } else {
            holder.checkBoxAddCity.setChecked(false);
        }

        holder.checkBoxAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subCategoryModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView cityName;
        CheckBox checkBoxAddCity;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.cityName);
            checkBoxAddCity = itemView.findViewById(R.id.checkBoxAddCity);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int item);
    }

}

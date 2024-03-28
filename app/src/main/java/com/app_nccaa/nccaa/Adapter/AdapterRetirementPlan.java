package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterRetirementPlan extends RecyclerView.Adapter<AdapterRetirementPlan.Viewholder> {

    private final OnItemClickListener listener;
    private Context mContext;
    private ArrayList<CityModel> subCategoryModelArrayList;


    public AdapterRetirementPlan(Context mContext, ArrayList<CityModel> subCategoryModelArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.listener = onItemClickListener;
        this.subCategoryModelArrayList = subCategoryModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.adapter_reirement_plan, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.setIsRecyclable(false);

        holder.cityName.setText(subCategoryModelArrayList.get(position).getName());

        holder.valueET.setText(subCategoryModelArrayList.get(position).getValue());


        holder.valueET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                listener.onItemValue(position, holder.valueET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView cityName;
        EditText valueET;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.cityName);
            valueET = itemView.findViewById(R.id.valueET);
        }
    }

    public interface OnItemClickListener {
        void onItemValue(int item, String value);
    }

}

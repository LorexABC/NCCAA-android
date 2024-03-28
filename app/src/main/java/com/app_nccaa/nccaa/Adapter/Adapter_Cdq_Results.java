package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.Certificate_Exam_Model;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class Adapter_Cdq_Results extends RecyclerView.Adapter<Adapter_Cdq_Results.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<Certificate_Exam_Model> certificateExamArrayList;


    public Adapter_Cdq_Results(Context context, ArrayList<Certificate_Exam_Model> certificateExamArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.certificateExamArrayList = certificateExamArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_cdq_bookings, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

      /*  if (position == 0){
            holder.book_testing_center_Btn.setVisibility(View.VISIBLE);
        } else {
            holder.book_testing_center_Btn.setVisibility(View.GONE);
        }*/

        holder.book_testing_center_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });


        if (certificateExamArrayList.get(position).getResultsAvailable().equals("true")){
            holder.book_testing_center_Btn.setBackground(context.getDrawable(R.drawable.capsul_blue_btn));
            holder.book_testing_center_Btn.setText("Results (available)");
        } else {
            holder.book_testing_center_Btn.setBackground(context.getDrawable(R.drawable.capsul_grey_btn));
            holder.book_testing_center_Btn.setText("Results (not available)");
        }


    }

    @Override
    public int getItemCount() {
        return certificateExamArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView book_testing_center_Btn;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            book_testing_center_Btn = itemView.findViewById(R.id.book_testing_center_Btn);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

}

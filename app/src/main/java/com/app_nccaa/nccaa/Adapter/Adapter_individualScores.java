package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.IndividualScores;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class Adapter_individualScores extends RecyclerView.Adapter<Adapter_individualScores.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<IndividualScores> individualScoresArrayList;


    public Adapter_individualScores(Context context, ArrayList<IndividualScores> individualScoresArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.individualScoresArrayList = individualScoresArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_individualscores, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });

        holder.mYour_Score.setText(individualScoresArrayList.get(position).getScore()+"");
        holder.mMax_Score.setText(individualScoresArrayList.get(position).getMaximumScore()+"");
        holder.mCorrect.setText(individualScoresArrayList.get(position).getPercentageCorrect()+"%");
        holder.mGT_National_Correct_1.setText(individualScoresArrayList.get(position).getNationalCorrect()+"");
        holder.mGT_National_Correct_2.setText(individualScoresArrayList.get(position).getNationalPercentageCorrect()+"%");
        holder.mName.setText(individualScoresArrayList.get(position).getName());

        Log.e("asasdad",individualScoresArrayList.size()+ "---" + position);
        if(position == 0){
            holder.mBackground.setBackground(context.getResources().getDrawable(R.drawable.orange_light_5dp_1));
        }else if(individualScoresArrayList.size()-1 == position){
            holder.mBackground.setBackground(context.getResources().getDrawable(R.drawable.orange_light_5dp_2));
            holder.mDivider.setVisibility(View.GONE);
        }else {
            holder.mBackground.setBackground(context.getResources().getDrawable(R.drawable.orange_light_5dp_0));
        }

    }

    @Override
    public int getItemCount() {
        return individualScoresArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView mYour_Score;
        private TextView mMax_Score;
        private TextView mCorrect;
        private TextView mGT_National_Correct_1;
        private TextView mGT_National_Correct_2;
        private TextView mName;
        private LinearLayout mBackground;
        private ImageView mDivider;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            mYour_Score = itemView.findViewById(R.id.mYour_Score);
            mMax_Score = itemView.findViewById(R.id.mMax_Score);
            mCorrect = itemView.findViewById(R.id.mCorrect);
            mGT_National_Correct_1 = itemView.findViewById(R.id.mGT_National_Correct_1);
            mGT_National_Correct_2 = itemView.findViewById(R.id.mGT_National_Correct_2);
            mName = itemView.findViewById(R.id.mName);
            mBackground = itemView.findViewById(R.id.mBackground);
            mDivider = itemView.findViewById(R.id.mDivider);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

}

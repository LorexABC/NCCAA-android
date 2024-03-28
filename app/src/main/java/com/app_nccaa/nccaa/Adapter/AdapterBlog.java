package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.BlogModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class AdapterBlog extends RecyclerView.Adapter<AdapterBlog.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<BlogModel> blogModelArrayList;


    public AdapterBlog(Context context, ArrayList<BlogModel> blogModelArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.blogModelArrayList = blogModelArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_blog, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }
        });

        holder.blogheader.setText(blogModelArrayList.get(position).getSubject());

    }

    @Override
    public int getItemCount() {
        return blogModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView blogheader;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            blogheader = itemView.findViewById(R.id.blogheader);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }


}

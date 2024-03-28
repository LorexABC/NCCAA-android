package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.Certificate_Exam_Model;
import com.app_nccaa.nccaa.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Adapter_Cdq_Register extends RecyclerView.Adapter<Adapter_Cdq_Register.Viewholder> {

    private Context context;
    private final OnItemClickListener mListener;
    private ArrayList<Certificate_Exam_Model> certificateExamArrayList;


    public Adapter_Cdq_Register(Context context, ArrayList<Certificate_Exam_Model> certificateExamArrayList, OnItemClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.certificateExamArrayList = certificateExamArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_cdq_register, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        if (position +1 == certificateExamArrayList.size()){
            holder.lineBottom.setVisibility(View.GONE);
        }


        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDate = new SimpleDateFormat("dd");
        DateFormat outputMonth = new SimpleDateFormat("MMMM");
        DateFormat outputYear = new SimpleDateFormat("yyyy");

        String inputDateStr = certificateExamArrayList.get(position).getDateStart();
        String inputDateEnd = certificateExamArrayList.get(position).getDateEnd();


        Date dateStart = null;
        try {
            dateStart = inputFormat.parse(inputDateStr);
            Date dateEnd = inputFormat.parse(inputDateEnd);
            Date monthD = inputFormat.parse(inputDateStr);
            Date yearD = inputFormat.parse(inputDateStr);

            String strDateStart = outputDate.format(dateStart);
            String strDateEnd = outputDate.format(dateEnd);
            String month = outputMonth.format(monthD);
            String year = outputYear.format(yearD);


            String mFinalStartDate = null;
            if (!strDateStart.equals("null")) {
                if (Integer.parseInt(strDateStart) < 10) {
                    mFinalStartDate = strDateStart.replace("0", "");
                } else {
                    mFinalStartDate = strDateStart;
                }
            }

            String mFinalEndDate = null;
            if (!strDateEnd.equals("null")) {
                if (Integer.parseInt(strDateEnd) < 10) {
                    mFinalEndDate = strDateEnd.replace("0", "");
                } else {
                    mFinalEndDate = strDateEnd;
                }
            }



            holder.titleExam.setText(month + " " + mFinalStartDate + "-" + mFinalEndDate + ", " + year + ", " + certificateExamArrayList.get(position).getType().toUpperCase(Locale.ROOT) + " Exam");
            holder.registerExamButton.setText("Register for the " + month + " Exam");

            holder.registrationExam.setText(chnage_date_formate(certificateExamArrayList.get(position).getRegistrationStart()) + " - " + chnage_date_formate(certificateExamArrayList.get(position).getRegistrationEnd()));
            holder.lateExam.setText(chnage_date_formate(certificateExamArrayList.get(position).getLateStart()) + " - " + chnage_date_formate(certificateExamArrayList.get(position).getLateEnd()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.registerExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemRegister(position);
            }
        });

        if (certificateExamArrayList.get(position).getRegistrationIsAvailable().equals("true")){
            holder.registerExamButton.setBackground(context.getDrawable(R.drawable.capsul_blue_btn));
        } else {
            holder.registerExamButton.setBackground(context.getDrawable(R.drawable.capsul_grey_btn));
        }


    }

    @Override
    public int getItemCount() {
        return certificateExamArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView titleExam, registrationExam, lateExam, registerExamButton;
        private RelativeLayout lineBottom;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            titleExam = itemView.findViewById(R.id.titleExam);
            registrationExam = itemView.findViewById(R.id.registrationExam);
            lateExam = itemView.findViewById(R.id.lateExam);
            registerExamButton = itemView.findViewById(R.id.registerExamButton);
            lineBottom = itemView.findViewById(R.id.lineBottom);

        }
    }

    public interface OnItemClickListener {
        void onItemRegister(int pos);
    }

    public static String chnage_date_formate(@NonNull String mDate) {
        String finalDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date oneWayTripDate = input.parse(mDate);  // parse input
            finalDate = output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalDate;
    }


}

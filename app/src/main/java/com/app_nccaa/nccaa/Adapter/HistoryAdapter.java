package com.app_nccaa.nccaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_nccaa.nccaa.Model.CDQModel;
import com.app_nccaa.nccaa.Model.CERTModel;
import com.app_nccaa.nccaa.Model.CMEModel;
import com.app_nccaa.nccaa.Model.RESULTModel;
import com.app_nccaa.nccaa.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final OnItemClickListener listener;
    private final Context mContext;
    private ArrayList<Object> mHistoryArrayList;
    public static final int CDQ = 0;
    public static final int CME = 1;
    public static final int RESULT = 2;
    public static final int CERT = 3;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolderCDQ extends RecyclerView.ViewHolder {
        private final TextView mDate_exam;
        private final TextView mDate_time;
        private final TextView mAmount_paid;
        private final TextView mPayment_period;
        private final TextView mAttempts;
        private final TextView viewTV;

        public MyViewHolderCDQ(View v) {
            super(v);
            this.mDate_exam = (TextView) itemView.findViewById(R.id.mDate_exam);
            this.mDate_time = (TextView) itemView.findViewById(R.id.mDate_time);
            this.mAmount_paid = (TextView) itemView.findViewById(R.id.mAmount_paid);
            this.mPayment_period = (TextView) itemView.findViewById(R.id.mPayment_period);
            this.mAttempts = (TextView) itemView.findViewById(R.id.mAttempts);
            this.viewTV = (TextView) itemView.findViewById(R.id.viewTV);
        }
    }

    public static class MyViewHolderCME extends RecyclerView.ViewHolder {
        private final TextView mPayment_period;
        private final TextView mDate_paid;
        private final TextView mAmount_paid;
        private final TextView mDue_date;
        private final TextView mCycle_period;
        private final TextView viewTV;

        public MyViewHolderCME(View v) {
            super(v);
            this.mPayment_period = (TextView) itemView.findViewById(R.id.mPayment_period);
            this.mAmount_paid = (TextView) itemView.findViewById(R.id.mAmount_paid);
            this.mDate_paid = (TextView) itemView.findViewById(R.id.mDate_paid);
            this.mDue_date = (TextView) itemView.findViewById(R.id.mDue_date);
            this.mCycle_period = (TextView) itemView.findViewById(R.id.mCycle_period);
            this.viewTV = (TextView) itemView.findViewById(R.id.viewTV);
        }
    }

    public static class MyViewHolderRESULT extends RecyclerView.ViewHolder {
        private final TextView mDate;
        private final TextView mResult, viewTV, examType;
        private final LinearLayout mLayout_bg;

        public MyViewHolderRESULT(View v) {
            super(v);
            this.mDate = (TextView) itemView.findViewById(R.id.mDate);
            this.mResult = (TextView) itemView.findViewById(R.id.mResult);
            this.mLayout_bg = (LinearLayout) itemView.findViewById(R.id.mLayout_bg);
            this.viewTV = (TextView) itemView.findViewById(R.id.viewTV);
            this.examType = (TextView) itemView.findViewById(R.id.examType);
        }
    }

    public static class MyViewHolderCERT extends RecyclerView.ViewHolder {
        private final TextView mUniCode;
        private final TextView mUniversity;
        private final TextView mAttempts;
        private final TextView mPayment_period;
        private final TextView mAmount_Paid;
        private final TextView mDate_Paid;
        private final TextView mDate_exam;
        private final TextView viewTV;

        public MyViewHolderCERT(View v) {
            super(v);
            this.mUniCode = (TextView) itemView.findViewById(R.id.mUniCode);
            this.mUniversity = (TextView) itemView.findViewById(R.id.mUniversity);
            this.mAttempts = (TextView) itemView.findViewById(R.id.mAttempts);
            this.mPayment_period = (TextView) itemView.findViewById(R.id.mPayment_period);
            this.mAmount_Paid = (TextView) itemView.findViewById(R.id.mAmount_Paid);
            this.mDate_Paid = (TextView) itemView.findViewById(R.id.mDate_Paid);
            this.mDate_exam = (TextView) itemView.findViewById(R.id.mDate_exam);
            this.viewTV = (TextView) itemView.findViewById(R.id.viewTV);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(Context context,ArrayList<Object> History_arrayList, OnItemClickListener listener) {
        mContext = context;
        mHistoryArrayList = History_arrayList;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == CDQ) {
            View recipeItem = inflater.inflate(R.layout.item_view_cdq, parent, false);
            return new MyViewHolderCDQ(recipeItem);
        }  else if (viewType == CME) {
            View nativeAdItem2 = inflater.inflate(R.layout.item_view_cme, parent, false);
            return new MyViewHolderCME(nativeAdItem2);
        } else if (viewType == RESULT) {
            View nativeAdItem2 = inflater.inflate(R.layout.item_view_result, parent, false);
            return new MyViewHolderRESULT(nativeAdItem2);
        }else if (viewType == CERT) {
            View nativeAdItem2 = inflater.inflate(R.layout.item_view_cert, parent, false);
            return new MyViewHolderCERT(nativeAdItem2);
        } else {
            return null;
        }
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int itemType = getItemViewType(position);

        if (itemType == CDQ) {
            MyViewHolderCDQ viewHolder = (MyViewHolderCDQ) holder;
            CDQModel bean = (CDQModel) mHistoryArrayList.get(position);


            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputDate = new SimpleDateFormat("dd");
            DateFormat outputMonth = new SimpleDateFormat("MMMM");
            DateFormat outputYear = new SimpleDateFormat("yyyy");

            String inputDateStr = bean.getExamDateStart();
            String inputDateEnd = bean.getExamDateEnd();

            try {
                Date dateStart = inputFormat.parse(inputDateStr);
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

                viewHolder.mDate_exam.setText(month + " " + mFinalStartDate + "-" + mFinalEndDate + ", " + year );
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd/mm/yyyy (hh:mm aa)");
            try {
                Date oneWayTripDate = input.parse(bean.getPaidDate().substring(0,19).replace("T"," "));  // parse input
                viewHolder.mDate_time.setText(output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            viewHolder.mAmount_paid.setText("$"+df.format(Float.parseFloat(bean.getPaidAmount())));
            viewHolder.mPayment_period.setText(bean.getPaymentPeriod().toUpperCase(Locale.ROOT));

            if(Integer.parseInt(bean.getAttemptNumber()) > 1){
                int mRetakeNum = Integer.parseInt(bean.getAttemptNumber()) - 1;
                viewHolder.mAttempts.setText(bean.getAttemptNumber() +" - ("+"Retake #" + mRetakeNum +")");
            }else {
                viewHolder.mAttempts.setText(bean.getAttemptNumber());
            }

            viewHolder.viewTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position, "cdq", bean.getReceiptId());
                }
            });




        }else if(itemType == CME) {
            MyViewHolderCME viewHolder = (MyViewHolderCME) holder;
            CMEModel bean = (CMEModel) mHistoryArrayList.get(position);
            //--Need this value from response for validation
            viewHolder.mPayment_period.setText(bean.getPaymentPeriod().toUpperCase(Locale.ROOT));
            //--Pending Date Formate
       //     viewHolder.mDate_paid.setText(bean.getPaidDate());
            viewHolder.mAmount_paid.setText("$" + bean.getPaidAmount());


            viewHolder.mCycle_period.setText(bean.getCycle());

            viewHolder.viewTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position, "cme", bean.getReceiptId());
                }
            });

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date oneWayTripDate = input.parse(bean.getDueDate());  // parse input
                viewHolder.mDue_date.setText(output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output1 = new SimpleDateFormat("dd/mm/yyyy (hh:mm aa)");
            try {
                Date oneWayTripDate = input1.parse(bean.getPaidDate().substring(0,19).replace("T"," "));  // parse input
                viewHolder.mDate_paid.setText(output1.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if(itemType == RESULT) {
            MyViewHolderRESULT viewHolder = (MyViewHolderRESULT) holder;
            RESULTModel bean = (RESULTModel) mHistoryArrayList.get(position);


            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date oneWayTripDate = input.parse(bean.getDate());  // parse input
                viewHolder.mDate.setText(output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.mResult.setText(bean.getResults().toUpperCase(Locale.ROOT));
            if(bean.getExamType().equals("cdq")){
                viewHolder.mLayout_bg.setBackground(mContext.getDrawable(R.drawable.blue_5dp));
                viewHolder.examType.setText("CDQ Exam");
            }else {
                viewHolder.mLayout_bg.setBackground(mContext.getDrawable(R.drawable.orange_light_5dp));
                viewHolder.examType.setText("Certification");
            }

            viewHolder.viewTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position, "result", bean.getExamId());
                }
            });

        }else if(itemType == CERT) {
            MyViewHolderCERT viewHolder = (MyViewHolderCERT) holder;
            CERTModel bean = (CERTModel) mHistoryArrayList.get(position);
            viewHolder.mUniCode.setText(bean.getUniversityCode().toUpperCase(Locale.ROOT));
            viewHolder.mUniversity.setText(bean.getUniversityName());
            if(Integer.parseInt(bean.getAttemptNumber()) > 1){
                int mRetakeNum = Integer.parseInt(bean.getAttemptNumber()) - 1;
                viewHolder.mAttempts.setText(bean.getAttemptNumber() +" - ("+"Retake #" + mRetakeNum +")");
            }else {
                viewHolder.mAttempts.setText(bean.getAttemptNumber());

            }

            viewHolder.mPayment_period.setText(bean.getPaymentPeriod().toUpperCase(Locale.ROOT));
            viewHolder.mAmount_Paid.setText("$"+df.format(Float.parseFloat(bean.getPaidAmount())));
            //--Pending Date Formate
            viewHolder.mDate_Paid.setText(bean.getPaidDate());
            //--Pending Date Formate

            @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") DateFormat outputDate = new SimpleDateFormat("dd");
            @SuppressLint("SimpleDateFormat") DateFormat outputMonth = new SimpleDateFormat("MMMM");
            @SuppressLint("SimpleDateFormat") DateFormat outputYear = new SimpleDateFormat("yyyy");

            String inputDateStr = bean.getExamDateStart();
            String inputDateEnd = bean.getExamDateEnd();

            try {
                Date dateStart = inputFormat.parse(inputDateStr);
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
                viewHolder.mDate_exam.setText(month + " " + mFinalStartDate + "-" + mFinalEndDate + ", " + year );
            } catch (ParseException e) {
                e.printStackTrace();
            }


            viewHolder.viewTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position, "", bean.getReceiptId());
                }
            });


        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mHistoryArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int item, String type, String id);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mHistoryArrayList.get(position);
        if (item instanceof CDQModel) {
            return CDQ;
        } else if(item instanceof CMEModel){
            return CME;
        } else if(item instanceof RESULTModel){
            return RESULT;
        } else if (item instanceof CERTModel){
            return CERT;
        }
        return position;
    }




}

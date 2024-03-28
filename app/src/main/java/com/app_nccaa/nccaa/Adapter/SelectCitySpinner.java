package com.app_nccaa.nccaa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.app_nccaa.nccaa.Model.CityModel;
import com.app_nccaa.nccaa.R;

import java.util.ArrayList;

public class SelectCitySpinner extends ArrayAdapter<CityModel> {

    private Context context;
    private ArrayList<CityModel> values;

    public SelectCitySpinner(Context context, int textViewResourceId,
                             ArrayList<CityModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        int count = super.getCount();
        return count > 0 ? count - 1 : count;

    }

    @Override
    public CityModel getItem(int position){
       return values.get(position);
    }

    @Override
    public long getItemId(int position){
       return position;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());

        TextView label = (TextView) super.getView(position, view, parent);
        label.setTextColor(context.getResources().getColor(R.color.light_black));
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
//        label.setTypeface(context.getResources().getFont(R.font.roboto_regular));

        label.setText(values.get(position).getName());

        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }
}
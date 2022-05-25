package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertRVAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView alert_name_tv;
        TextView year_tv;
        TextView month_tv;
        TextView day_tv;
        TextView state_tv;

        MyViewHolder(View view){
            super(view);
            alert_name_tv = view.findViewById(R.id.alert_name_tv);
            year_tv = view.findViewById(R.id.alert_year_tv);
            month_tv = view.findViewById(R.id.alert_month_tv);
            day_tv = view.findViewById(R.id.alert_day_tv);
            state_tv = view.findViewById(R.id.state_tv);
        }
    }

    private ArrayList<Alert> alertArrayList;
    AlertRVAdapter(ArrayList<Alert> list) {
        this.alertArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert,parent,false);
        return new AlertRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlertRVAdapter.MyViewHolder myViewHolder = (AlertRVAdapter.MyViewHolder) holder;

        myViewHolder.alert_name_tv.setText(alertArrayList.get(position).alertName);
        myViewHolder.year_tv.setText(alertArrayList.get(position).year + "");
        myViewHolder.month_tv.setText(alertArrayList.get(position).month + "");
        myViewHolder.day_tv.setText(alertArrayList.get(position).day+ "");
        myViewHolder.state_tv.setText(alertArrayList.get(position).state + "");
    }

    @Override
    public int getItemCount() {
        return alertArrayList.size();
    }

}
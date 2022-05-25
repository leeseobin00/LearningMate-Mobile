package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertMentorRVAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView alertmentor_name_tv;
        TextView statementor_tv;

        MyViewHolder(View view){
            super(view);
            alertmentor_name_tv = view.findViewById(R.id.alertmentor_name_tv);
            statementor_tv = view.findViewById(R.id.statementor_tv);
        }
    }

    private ArrayList<AlertMentor> alertMentorArrayList;
    AlertMentorRVAdapter(ArrayList<AlertMentor> list) {
        this.alertMentorArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alertmentor,parent,false);
        return new AlertMentorRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlertMentorRVAdapter.MyViewHolder myViewHolder = (AlertMentorRVAdapter.MyViewHolder) holder;

        myViewHolder.alertmentor_name_tv.setText(alertMentorArrayList.get(position).alertName);
        myViewHolder.statementor_tv.setText(alertMentorArrayList.get(position).state + "");
    }

    @Override
    public int getItemCount() {
        return alertMentorArrayList.size();
    }

}
package com.example.learningmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learningmate.R;
import com.example.learningmate.model.Alert;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertRVAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static class AssignmentHolder extends RecyclerView.ViewHolder{
        TextView assignName;
        TextView date;
        TextView due;
        TextView state;

        public AssignmentHolder(View view){
            super(view);
            assignName = view.findViewById(R.id.alert_name_tv);
            date = view.findViewById(R.id.alert_date_tv);
            due = view.findViewById(R.id.alert_year_tv);
            state = view.findViewById(R.id.state_tv);
        }
    }

    public static class MaterialHolder extends RecyclerView.ViewHolder{
        TextView assignName;
        TextView date;
        public MaterialHolder(View view){
            super(view);
            assignName = view.findViewById(R.id.alert_name_tv);
            date = view.findViewById(R.id.alert_date_tv);
        }
    }

    private ArrayList<Alert> alertArrayList;
    public AlertRVAdapter(ArrayList<Alert> list) {
        this.alertArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == 0){
            view = inflater.inflate(R.layout.item_alert, parent, false);
            return new AssignmentHolder(view);
        }
        else if(viewType == 1){
            view = inflater.inflate(R.layout.item_alert_m, parent, false);
            return new MaterialHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert,parent,false);
        return new AssignmentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AssignmentHolder){
            AssignmentHolder myViewHolder = (AssignmentHolder) holder;
            myViewHolder.assignName.setText(alertArrayList.get(position).getAlertName());
            myViewHolder.date.setText("[과제 등록 날짜: "+alertArrayList.get(position).getUploadDate()+"]");
            myViewHolder.due.setText(alertArrayList.get(position).getDueDate());
            myViewHolder.state.setText(alertArrayList.get(position).isState() ? "제출 완료" : "미제출");
        }
        else if(holder instanceof MaterialHolder){
            MaterialHolder myViewHolder = (MaterialHolder) holder;
            myViewHolder.assignName.setText(alertArrayList.get(position).getAlertName());
            myViewHolder.date.setText("[업로드 날짜: "+alertArrayList.get(position).getUploadDate()+"]");
        }
    }

    @Override
    public int getItemCount() {
        return alertArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return alertArrayList.get(position).getViewType();
    }
}
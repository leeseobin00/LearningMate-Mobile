package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProblemRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView problem_tv;
        TextView answer1_tv;
        TextView answer2_tv;
        TextView answer3_tv;
        TextView answer4_tv;

        MyViewHolder(View view){
            super(view);
            problem_tv = view.findViewById(R.id.question_tv);
            answer1_tv = view.findViewById(R.id.answer1_tv);
            answer2_tv = view.findViewById(R.id.answer2_tv);
            answer3_tv = view.findViewById(R.id.answer3_tv);
            answer4_tv = view.findViewById(R.id.answer4_tv);
        }
    }

    private ArrayList<Problem> problemArrayList;
    ProblemRVAdapter(ArrayList<Problem> list) {
        this.problemArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem,parent,false);
        return new ProblemRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProblemRVAdapter.MyViewHolder myViewHolder = (ProblemRVAdapter.MyViewHolder) holder;

        myViewHolder.problem_tv.setText(problemArrayList.get(position).problem);
        myViewHolder.answer1_tv.setText(problemArrayList.get(position).answer1 + "");
        myViewHolder.answer2_tv.setText(problemArrayList.get(position).answer2 + "");
        myViewHolder.answer3_tv.setText(problemArrayList.get(position).answer3+ "");
        myViewHolder.answer4_tv.setText(problemArrayList.get(position).answer4 + "");
    }

    @Override
    public int getItemCount() {
        return problemArrayList.size();
    }

}

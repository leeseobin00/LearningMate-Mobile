package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizmakerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quiz_num_tv;

        MyViewHolder(View view){
            super(view);
            quiz_num_tv = view.findViewById(R.id.quiz_num_tv);
        }
    }

    private ArrayList<Quizmaker> quizmakerArrayList;
    QuizmakerRVAdapter(ArrayList<Quizmaker> list) {
        this.quizmakerArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quizmaker,parent,false);
        return new QuizmakerRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuizmakerRVAdapter.MyViewHolder myViewHolder = (QuizmakerRVAdapter.MyViewHolder) holder;

        myViewHolder.quiz_num_tv.setText(quizmakerArrayList.get(position).quizNum);
    }

    @Override
    public int getItemCount() {
        return quizmakerArrayList.size();
    }

}

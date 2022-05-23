package com.example.learningmate;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quiz_name_tv;

        MyViewHolder(View view){
            super(view);
            quiz_name_tv = view.findViewById(R.id.quiz_name_tv);
        }
    }


    private ArrayList<Quiz> quizArrayList;
    QuizRVAdapter(ArrayList<Quiz> list) {
        this.quizArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.quiz_name_tv.setText(quizArrayList.get(position).quizName);
    }


    @Override
    public int getItemCount() {
        return quizArrayList.size();
    }

}

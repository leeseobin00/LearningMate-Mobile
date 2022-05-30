package com.example.learningmate.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learningmate.R;
import com.example.learningmate.model.Quiz;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //퀴즈 아이템 누르면 problemActivity로 전환하기 위하여 필요한 것
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_name_tv;
        TextView score;
        TextView totalScore;

        MyViewHolder(View view) {
            super(view);
            quiz_name_tv = view.findViewById(R.id.quiz_name_tv);
            score = view.findViewById(R.id.score_tv);
            totalScore = view.findViewById(R.id.totalscore_tv);
            //퀴즈 아이템 누르면 problemActivity로 전환하기 위하여 필요한 것
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }


    private ArrayList<Quiz> quizArrayList;

    public QuizRVAdapter(ArrayList<Quiz> list) {
        this.quizArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.quiz_name_tv.setText(quizArrayList.get(position).getTitle());
        myViewHolder.score.setText(quizArrayList.get(position).getQuizGrade() == -1 ? "-" : quizArrayList.get(position).getQuizGrade() + "");
        myViewHolder.totalScore.setText(quizArrayList.get(position).getPerfectScore() + "");
    }


    @Override
    public int getItemCount() {
        return quizArrayList.size();
    }

}
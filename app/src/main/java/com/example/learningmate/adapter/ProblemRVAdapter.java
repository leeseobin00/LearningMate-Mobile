package com.example.learningmate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.learningmate.R;
import com.example.learningmate.model.Problem;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProblemRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView problem_tv;
        TextView point;
        TextView answer1_tv;
        TextView answer2_tv;
        TextView answer3_tv;
        TextView answer4_tv;
        RadioGroup selection;

        MyViewHolder(View view) {
            super(view);
            problem_tv = view.findViewById(R.id.question_tv);
            point = view.findViewById(R.id.point_tv);
            answer1_tv = view.findViewById(R.id.answer1_tv);
            answer2_tv = view.findViewById(R.id.answer2_tv);
            answer3_tv = view.findViewById(R.id.answer3_tv);
            answer4_tv = view.findViewById(R.id.answer4_tv);
            selection = view.findViewById(R.id.q1RadioGroup);
        }
    }

    private ArrayList<Problem> problemArrayList;

    public ProblemRVAdapter(ArrayList<Problem> list) {
        this.problemArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem, parent, false);
        return new ProblemRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProblemRVAdapter.MyViewHolder myViewHolder = (ProblemRVAdapter.MyViewHolder) holder;
        int pos = position;
        myViewHolder.problem_tv.setText(problemArrayList.get(position).getProblem());
        myViewHolder.point.setText(problemArrayList.get(position).getPoint() + "점");
        myViewHolder.answer1_tv.setText(problemArrayList.get(position).getChoice1());
        myViewHolder.answer2_tv.setText(problemArrayList.get(position).getChoice2());
        myViewHolder.answer3_tv.setText(problemArrayList.get(position).getChoice3());
        myViewHolder.answer4_tv.setText(problemArrayList.get(position).getChoice4());
        myViewHolder.selection.check(problemArrayList.get(pos).getSelectedId());
        myViewHolder.selection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.answer1_tv) {
                    problemArrayList.get(pos).setSelectAnswer(problemArrayList.get(pos).getChoice1());
                } else if (i == R.id.answer2_tv) {
                    problemArrayList.get(pos).setSelectAnswer(problemArrayList.get(pos).getChoice2());
                } else if (i == R.id.answer3_tv) {
                    problemArrayList.get(pos).setSelectAnswer(problemArrayList.get(pos).getChoice3());
                } else if (i == R.id.answer4_tv) {
                    problemArrayList.get(pos).setSelectAnswer(problemArrayList.get(pos).getChoice4());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return problemArrayList.size();
    }

}

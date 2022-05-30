package com.example.learningmate.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.learningmate.model.QuizMaker;
import com.example.learningmate.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuizmakerRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView questionNumber;
        private EditText question;
        private EditText point;
        private EditText choice1;
        private EditText choice2;
        private EditText choice3;
        private EditText choice4;
        private EditText answer;
        MyViewHolder(View view) {
            super(view);
            questionNumber = view.findViewById(R.id.quiz_num_tv);
            question = view.findViewById(R.id.question1_et);
            point = view.findViewById(R.id.q1_point);
            choice1 = view.findViewById(R.id.q1answer1_et);
            choice2 = view.findViewById(R.id.q1answer2_et);
            choice3 = view.findViewById(R.id.q1answer3_et);
            choice4 = view.findViewById(R.id.q1answer4_et);
            answer = view.findViewById(R.id.q1_answer);
        }

        public EditText getQuestion() {
            return question;
        }

        public EditText getPoint() {
            return point;
        }

        public EditText getChoice1() {
            return choice1;
        }

        public EditText getChoice2() {
            return choice2;
        }

        public EditText getChoice3() {
            return choice3;
        }

        public EditText getChoice4() {
            return choice4;
        }
    }

    private ArrayList<QuizMaker> quizMakerArrayList;

    public QuizmakerRVAdapter(ArrayList<QuizMaker> list) {
        this.quizMakerArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quizmaker, parent, false);
        return new QuizmakerRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuizmakerRVAdapter.MyViewHolder myViewHolder = (QuizmakerRVAdapter.MyViewHolder) holder;
        int pos = position;
        myViewHolder.questionNumber.setText(quizMakerArrayList.get(position).quizNum);
        myViewHolder.question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setQuestion(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myViewHolder.point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setPoint(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myViewHolder.choice1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setChoice1(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myViewHolder.choice2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setChoice2(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myViewHolder.choice3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setChoice3(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        myViewHolder.choice4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setChoice4(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myViewHolder.answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quizMakerArrayList.get(pos).setAnswer(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return quizMakerArrayList.size();
    }

}

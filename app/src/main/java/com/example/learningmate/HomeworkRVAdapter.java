package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeworkRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private HomeworkRVAdapter.OnItemClickListener mListener = null;



    private int position;

    public void setOnItemClickListener(HomeworkRVAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    /*void addItem(Homework homework){
        homeworkArrayList.add(homework);
        notifyDataSetChanged();
    }*/

    public void removeItem(int position){
        homeworkArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView homework_name_tv;
        TextView dueDate;
        TextView totalScore;
        TextView gradedScore;
//        TextView year_tv;
//        TextView month_tv;
//        TextView day_tv;
//        TextView score_tv;

        MyViewHolder(View view){
            super(view);

            homework_name_tv = view.findViewById(R.id.homework_name_tv);
            dueDate = view.findViewById(R.id.homework_year_tv);
            totalScore = view.findViewById(R.id.totalscore_tv);
            gradedScore = view.findViewById(R.id.score_tv);
//            year_tv = view.findViewById(R.id.homework_year_tv);
//            month_tv = view.findViewById(R.id.homework_month_tv);
//            day_tv = view.findViewById(R.id.homework_day_tv);
//            score_tv = view.findViewById(R.id.score_tv);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition ();
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });
        }
    }


    private ArrayList<Homework> homeworkArrayList;
    HomeworkRVAdapter(ArrayList<Homework> list) {
        this.homeworkArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework,parent,false);
        return new HomeworkRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeworkRVAdapter.MyViewHolder myViewHolder = (HomeworkRVAdapter.MyViewHolder) holder;

        myViewHolder.homework_name_tv.setText(homeworkArrayList.get(position).getTitle());
        myViewHolder.dueDate.setText(homeworkArrayList.get(position).getDueDate());
        myViewHolder.totalScore.setText(homeworkArrayList.get(position).getPerfectScore()+"");
        myViewHolder.gradedScore.setText((homeworkArrayList.get(position).getGradedScore() == -1 ? "-" : homeworkArrayList.get(position).getGradedScore()) +"");
//        myViewHolder.month_tv.setText(homeworkArrayList.get(position).month + "");
//        myViewHolder.day_tv.setText(homeworkArrayList.get(position).day+ "");
//        myViewHolder.score_tv.setText(homeworkArrayList.get(position).score + "");

    }


    @Override
    public int getItemCount() {
        return homeworkArrayList.size();
    }

}
package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FileRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private FileRVAdapter.OnItemClickListener mListener = null;
    public void setOnItemClickListener(FileRVAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView file_name_tv;
        TextView attempt_num_tv;
        TextView file_status_tv;
        TextView year_tv;
        TextView month_tv;
        TextView day_tv;
        TextView score_tv;

        MyViewHolder(View view){
            super(view);
            file_name_tv = view.findViewById(R.id.file_name_tv);
            attempt_num_tv = view.findViewById(R.id.file_attemptnum_tv);
            file_status_tv = view.findViewById(R.id.file_status_tv);
            year_tv = view.findViewById(R.id.file_year_tv);
            month_tv = view.findViewById(R.id.file_month_tv);
            day_tv = view.findViewById(R.id.file_day_tv);

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

    private ArrayList<File> fileArrayList;
    FileRVAdapter(ArrayList<File> list) {
        this.fileArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file,parent,false);
        return new FileRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FileRVAdapter.MyViewHolder myViewHolder = (FileRVAdapter.MyViewHolder) holder;

        myViewHolder.file_name_tv.setText(fileArrayList.get(position).fileName);
        myViewHolder.attempt_num_tv.setText(fileArrayList.get(position).attemptNum);
        myViewHolder.file_status_tv.setText(fileArrayList.get(position).status);
        myViewHolder.year_tv.setText(fileArrayList.get(position).year + "");
        myViewHolder.month_tv.setText(fileArrayList.get(position).month + "");
        myViewHolder.day_tv.setText(fileArrayList.get(position).day+ "");
        myViewHolder.score_tv.setText(fileArrayList.get(position).score + "");
    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }

}

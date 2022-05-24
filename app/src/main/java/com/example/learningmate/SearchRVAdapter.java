package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView user_name;
        TextView user_id;

        MyViewHolder(View view){
            super(view);
            user_name = view.findViewById(R.id.search_user_name_tv);
            user_id = view.findViewById(R.id.search_user_id_tv);
        }
    }


    private ArrayList<User> userArrayList;
    SearchRVAdapter(ArrayList<User> list) {
        this.userArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new SearchRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchRVAdapter.MyViewHolder myViewHolder = (SearchRVAdapter.MyViewHolder) holder;

        myViewHolder.user_name.setText(userArrayList.get(position).getUserName());
        myViewHolder.user_id.setText(userArrayList.get(position).getUserId());

    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public void  filterList(ArrayList<User> filteredList) {
        userArrayList = filteredList;
        notifyDataSetChanged();
    }
}

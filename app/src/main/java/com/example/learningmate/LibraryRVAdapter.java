package com.example.learningmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LibraryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    interface OnItemLongClickListerner{
        void onItemLongClick(View v, int position);
    }
    private LibraryRVAdapter.OnItemClickListener mListener = null;
    private LibraryRVAdapter.OnItemLongClickListerner longClickListerner = null;
    private int position;

    public void setOnItemClickListener(LibraryRVAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(LibraryRVAdapter.OnItemLongClickListerner listener){
        this.longClickListerner = listener;
    }
    public int getPosition(){
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView libraryName;
        TextView fileName;


        MyViewHolder(View view){
            super(view);

            libraryName = view.findViewById(R.id.library_name_tv);
            fileName = view.findViewById(R.id.library_file_name_tv);

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        if(longClickListerner != null){
                            longClickListerner.onItemLongClick(view, position);
                        }
                    }
                    return true;
                }
            });
        }
    }


    private ArrayList<Library> libraryArrayList;
    LibraryRVAdapter(ArrayList<Library> list) {
        this.libraryArrayList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library,parent,false);
        return new LibraryRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        LibraryRVAdapter.MyViewHolder myViewHolder = (LibraryRVAdapter.MyViewHolder) holder;

        myViewHolder.libraryName.setText(libraryArrayList.get(position).getTitle());
        myViewHolder.fileName.setText(libraryArrayList.get(position).getFileName());
    }

    @Override
    public int getItemCount() {
        return libraryArrayList.size();
    }
}

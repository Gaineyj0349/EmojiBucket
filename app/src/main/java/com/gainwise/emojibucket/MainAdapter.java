package com.gainwise.emojibucket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>{

    ArrayList<EmojiBlock> list;
    Context context;

    public MainAdapter(ArrayList<EmojiBlock> list, Context context) {
        this.list = list;
        this.context = context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_forrv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if((ViewGroup)list.get(position).getParent() == null){
            holder.linearLayout.removeAllViews();
            holder.linearLayout.addView(list.get(position));
        }else{
            ((ViewGroup)list.get(position).getParent()).removeView(list.get(position));

            holder.linearLayout.addView(list.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if(list!= null && list.size()>0){
            return list.size();
        }
        else{
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
       LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.mainRV_card);
        }

    }
}

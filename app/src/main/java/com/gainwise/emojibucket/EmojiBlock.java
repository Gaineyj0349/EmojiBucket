package com.gainwise.emojibucket;

import android.content.Context;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static android.widget.LinearLayout.VERTICAL;

public class EmojiBlock extends CardView {

    EmojiBlockAdapter adapter;

    public EmojiBlock(Context context, ArrayList<Emoji> list, boolean recents) {
        super(context);
        this.setElevation(8f);


        //create layout to contain everything
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(VERTICAL);



        //create textview to give label
        EmojiAppCompatTextView textView = new EmojiAppCompatTextView(context);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        if(recents){
            textView.setText("Recents");
        }else{
            StringBuilder sb = new StringBuilder();
            sb.append(list.get(0).getGroup());
            sb.append("\t");
            sb.append(list.get(0).getSubGroup());
            textView.setText(sb.toString());
        }


        //create recyclerview to hold items
        RecyclerView recyclerView = new RecyclerView(context);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

         adapter = new EmojiBlockAdapter(list, context, recents);
        recyclerView.setAdapter(adapter);


        //add the views
        linearLayout.addView(textView);
        linearLayout.addView(recyclerView);
        this.addView(linearLayout);

    }

    public void notifyChange(){
        this.adapter.notifyDataSetChanged();
    }


}

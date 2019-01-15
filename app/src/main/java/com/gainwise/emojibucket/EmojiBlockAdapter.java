package com.gainwise.emojibucket;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiAppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class EmojiBlockAdapter extends RecyclerView.Adapter<EmojiBlockAdapter.MyViewHolder>{

    List<Emoji> list;
    Context context;
    boolean recents;




    public EmojiBlockAdapter(List<Emoji> list, Context context, boolean recents) {
        this.list = list;
        this.context = context;
        this.recents = recents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            holder.tv.setText(list.get(position).getEmojiString());


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
        EmojiAppCompatButton tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.emojiTV);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String emoji =  tv.getText().toString();
                    if(Utils.quickCopyMode(context)){
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("emoji",emoji);
                        clipboard.setPrimaryClip(clip);
                        Utils.toastShort(context, emoji);
                    }else{
                        ((MainActivity)context).appendET(emoji);
//                        StringBuilder sb = new StringBuilder(Utils.getCopyMessage(context));
//                        sb.append(emoji);
//                      Utils.setCopyMessage(context,sb.toString());
//                      Log.d("EMOJI92a", "called with "+ sb.toString());
                    }
                    if(Utils.needsToSaveRecent(context, emoji)){
                        ArrayList<String> tempList = Utils.getRecentsList(context);
                        tempList.add(0,emoji);
                        if(tempList.size()>20){
                            tempList.remove(20);
                        }
                        Utils.saveTempList(context, tempList);


                    }

                }
            });
        }

    }
}

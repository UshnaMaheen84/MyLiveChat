package com.example.admin.mylivechat.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.example.admin.mylivechat.R;
import com.github.library.bubbleview.BubbleTextView;

public class MyVH extends RecyclerView.ViewHolder {

    public TextView messageUserName, messageTime, message;
    public LinearLayout linearLayout;
    public BubbleLayout bubbleLayout;

    public MyVH(@NonNull View itemView) {
        super(itemView);

        messageUserName = itemView.findViewById(R.id.message_user);
        messageTime = itemView.findViewById(R.id.message_time);
        message = (TextView) itemView.findViewById(R.id.bubbleText);
        linearLayout= itemView.findViewById(R.id.bubbleLinearLayout);
        bubbleLayout= itemView.findViewById(R.id.bubbleLayout);

    }
}
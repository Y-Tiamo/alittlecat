package com.yyq.cat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyq.cat.R;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Author: TiAmo
 * Date: 2020/8/27 15:24
 * Description:
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private static final int RECEIVE_MESSAGE=0;
    private static final int SEND_MESSAGE=1;
    private List<Message> messageList;
    UserInfo userInfo= JMessageClient.getMyInfo();
    private Context context;
    public ChatListAdapter(Context context) {
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFromUser().getUserName().equals(userInfo.getUserName())){
            return SEND_MESSAGE;
        }
        return RECEIVE_MESSAGE;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_message_left_right,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    public void setDate(List<Message> messageList){
        this.messageList=messageList;
    }
    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position)==RECEIVE_MESSAGE){
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            TextContent content=(TextContent) messageList.get(position).getContent();
            holder.leftText.setText(content.getText());
        }else if (getItemViewType(position)==SEND_MESSAGE){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            TextContent content=(TextContent) messageList.get(position).getContent();
            holder.rightText.setText(content.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout leftLayout;
        private LinearLayout rightLayout;
        private TextView leftText;
        private TextView rightText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout=itemView.findViewById(R.id.left_layout);
            rightLayout=itemView.findViewById(R.id.right_layout);
            leftText=itemView.findViewById(R.id.left_msg);
            rightText=itemView.findViewById(R.id.right_msg);
        }
    }
}

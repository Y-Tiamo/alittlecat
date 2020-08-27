package com.yyq.cat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyq.cat.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

/**
 * Author: TiAmo
 * Date: 2020/8/27 12:20
 * Description:
 */
public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {

    private List<Conversation> conversationList;
    private Context mContext;
    private OnConversationItemClickListener onConversationItemClickListener;

    public ConversationListAdapter(Context context) {
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_conversation_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.textView.setText(conversationList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConversationItemClickListener!=null){
                    int pos=holder.getAdapterPosition();
                    onConversationItemClickListener.beginConversation(pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onConversationItemClickListener!=null){
                    int pos=holder.getAdapterPosition();
                    onConversationItemClickListener.deleteConversation(pos);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.conversation_list);
        }
    }

    public interface OnConversationItemClickListener{
        void beginConversation(int position);
        void deleteConversation(int position);
    }

    public void setOnConversationItemClickListener(OnConversationItemClickListener onConversationItemClickListener){
        this.onConversationItemClickListener=onConversationItemClickListener;
    }

    public void setDate(List<Conversation> conversationList){
        this.conversationList=conversationList;
    }
}

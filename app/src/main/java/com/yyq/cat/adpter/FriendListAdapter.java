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

/**
 * Author: TiAmo
 * Date: 2020/8/25 8:48
 * Description:
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<String> nickNameList;
    private List<String> userNameList;
    private Context mContext;
    public OnFriendItemClickListener onFriendItemClickListener;


    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    public FriendListAdapter(Context context, List<String> nickNameList, List<String> userNameList) {
        this.mContext=context;
        this.nickNameList=nickNameList;
        this.userNameList=userNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (viewType == 0) {
            view=LayoutInflater.from(mContext).inflate(R.layout.layout_friend_list_c,parent,false);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_friend_list_item,parent,false);
        }
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (getItemViewType(position)==TYPE_HEADER){
            return;
        }
        if (holder instanceof ViewHolder){
            holder.nickName.setText(nickNameList.get(position));
            holder.userName.setText(userNameList.get(position));
            if (onFriendItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=holder.getAdapterPosition();
                        onFriendItemClickListener.showFriendList(pos);
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (0==position){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return nickNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nickName;
        private TextView userName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName=itemView.findViewById(R.id.tv_friend_nickname);
            userName=itemView.findViewById(R.id.tv_friend_username);
        }
    }

    public interface OnFriendItemClickListener{
        void showFriendList(int position);
    }
    public void setOnFriendItemClickListener(OnFriendItemClickListener onFriendItemClickListener){
        this.onFriendItemClickListener=onFriendItemClickListener;
    }
}

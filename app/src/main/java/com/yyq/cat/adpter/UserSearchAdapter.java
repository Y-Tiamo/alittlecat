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
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {
    private List<String> nameList;
    private List<String> passList;
    private Context mContext;
    public OnUserItemClickListener onUserItemClickListener;

    public UserSearchAdapter(Context context,List<String> nameList,List<String> passList) {
        this.mContext=context;
        this.nameList=nameList;
        this.passList=passList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_search_user_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvName.setText(nameList.get(position));
        holder.tvPass.setText(passList.get(position));
        if (onUserItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getAdapterPosition();
                    onUserItemClickListener.addFriend(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvPass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_user_name);
            tvPass=itemView.findViewById(R.id.tv_pass);
        }
    }

    public interface OnUserItemClickListener{
        void addFriend(int position);
    }
    public void setOnUserItemClickListener(OnUserItemClickListener onUserItemClickListener){
        this.onUserItemClickListener=onUserItemClickListener;
    }
}

package com.yyq.cat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyq.cat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: TiAmo
 * Date: 2020/8/23 22:09
 * Description:
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<Integer> imageList;//图标列表
    private List<String> menuTextList;//功能描述列表
    private List<Integer> heightList;//高度列表
    public onItemClickListener listener;

    private Context mContext;

    public RecyclerViewAdapter(Context context,List<Integer> imageList,List<String> menuTextList) {
        mContext=context;
        this.imageList=imageList;
        this.menuTextList=menuTextList;
        getRandomHeight();
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_function,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, int position) {
        ViewGroup.LayoutParams params=holder.itemView.getLayoutParams();
        params.height=heightList.get(position);
        holder.itemView.setLayoutParams(params);
        holder.image.setImageResource(imageList.get(position));
        holder.text.setText(menuTextList.get(position));
        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getAdapterPosition();
                    listener.ItemClickListener(pos);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.function_image);
            text=itemView.findViewById(R.id.function_text);
        }
    }

    private void getRandomHeight(){
        heightList=new ArrayList<>();
        for (int i=0;i<imageList.size();i++){
            heightList.add((int)(10+Math.random())*30);
        }
    }

    public interface onItemClickListener{
        void ItemClickListener(int position);
    }

    public void setOnClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}

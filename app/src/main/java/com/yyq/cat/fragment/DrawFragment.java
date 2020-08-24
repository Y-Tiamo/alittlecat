package com.yyq.cat.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yyq.cat.R;
import com.yyq.cat.adpter.RecyclerViewAdapter;
import com.yyq.cat.layout.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawFragment extends Fragment implements View.OnClickListener {

    private Button btnWeb;//动态
    private RecyclerView recyclerView;//功能列表
    private List<Integer> imageList;//图标列表
    private List<String> menuTextList;//功能描述列表
    private RecyclerViewAdapter adapter;//适配器

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DrawFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DrawFragment newInstance(String param1, String param2) {
        DrawFragment fragment = new DrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_draw,container,false);
        btnWeb=view.findViewById(R.id.btn_to_web);
        recyclerView=view.findViewById(R.id.recycler_view);

        btnWeb.setOnClickListener(this);

        //初始化列表
        initList();
        adapter=new RecyclerViewAdapter(getContext(),imageList,menuTextList);
        adapter.setOnClickListener(new RecyclerViewAdapter.onItemClickListener() {
            @Override
            public void ItemClickListener(int position) {
                Toast.makeText(getContext(), "点击了"+menuTextList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initList() {
        initImage();
        initText();

    }

    private void initImage() {
        imageList=new ArrayList<>();
        for (int i=0;i<=30;i++){
            imageList.add(R.drawable.ic_zone);
        }
    }

    private void initText() {
        menuTextList=new ArrayList<>();
        for (int i=0;i<=30;i++){
            menuTextList.add("菜单"+(i+1));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_to_web:
                Toast.makeText(getContext(), "动态", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

//    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
//
//        public RecyclerViewAdapter() {
//            super();
//        }
//
//        @NonNull
//        @Override
//        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//            View view=LayoutInflater.from(getContext()).inflate(R.layout.layout_recycler_function,parent,false);
//            ViewHolder holder=new ViewHolder(view);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
//            holder.image.setImageResource(imageList.get(position));
//            holder.text.setText(menuTextList.get(position));
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return imageList.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder{
//            public ImageView image;
//            public TextView text;
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                image=itemView.findViewById(R.id.function_image);
//                text=itemView.findViewById(R.id.function_text);
//            }
//        }
//
//        private void getRandomHeight(){
//            heightList=new ArrayList<>();
//            for (int i=0;i<imageList.size();i++){
//                heightList.add((int)(200+Math.random())*100);
//            }
//        }
//
//        public interface onItemClickListener{
//            void ItemClickListener(View view,int position);
//        }
//    }
}
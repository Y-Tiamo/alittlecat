package com.yyq.cat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyq.cat.R;
import com.yyq.cat.adpter.ConversationListAdapter;
import com.yyq.cat.chatroom.ShowMessageActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment
        implements ConversationListAdapter.OnConversationItemClickListener {

    private RecyclerView rvConversationList;
    private List<Conversation> conversationList;
    private ConversationListAdapter conversationListAdapter;

    public JournalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_journal, container, false);
        conversationList = new ArrayList<>();
        rvConversationList = view.findViewById(R.id.conversation_list);
        conversationListAdapter = new ConversationListAdapter(getContext());

        initConversationList();

        if (conversationList!=null) {
            rvConversationList.setLayoutManager(new LinearLayoutManager(getContext()));
            conversationListAdapter.setOnConversationItemClickListener(this);
            rvConversationList.setAdapter(conversationListAdapter);
            conversationListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "没有回话", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void initConversationList() {
        if (conversationList!=null){
            conversationList.clear();
        }
        if (JMessageClient.getConversationList().size() > 0) {
            for (Conversation conversation : JMessageClient.getConversationList()) {
                conversationList.add(conversation);
            }
            conversationListAdapter.setDate(conversationList);
        }
    }

    @Override
    public void beginConversation(final int position) {
        Conversation conversation = conversationList.get(position);
        Intent intent = new Intent(getContext(), ShowMessageActivity.class);

        intent.putExtra("friend_nick", conversationList.get(position).getTargetId());
        startActivity(intent);
    }

    @Override
    public void deleteConversation(final int position) {
        final Conversation conversation = conversationList.get(position);
        AlertDialog dialog=new AlertDialog.Builder(getContext()).create();
        dialog.setTitle("Tip");
        dialog.setMessage("此操作将会删除该会话记录，确认删除吗？");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Conversation conversation1=conversationList.get(position);
                JMessageClient.deleteSingleConversation(conversation.getTargetId());
                initConversationList();
                conversationListAdapter.notifyDataSetChanged();
//                JSONObject
                Log.d("Conversation",conversation.toJson());
                Toast.makeText(getContext(), "删除会话:"+conversation.getTargetId(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
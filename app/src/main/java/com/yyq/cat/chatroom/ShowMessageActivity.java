package com.yyq.cat.chatroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.yyq.cat.MyApplication;
import com.yyq.cat.R;
import com.yyq.cat.adpter.ChatListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

public class ShowMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="ShowMessageActivity";

    private TextView tvFriendName;
    private RecyclerView rvChatList;
    private ChatListAdapter chatListAdapter;

    private List<Message> messageList;

    private Conversation conversation;

    private EditText etMessageContent;
    private Button btnSendMessage;
    private String name;

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        setContentView(R.layout.activity_show_message);

        initView();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG,"onReStart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }

    private void initView() {
        Intent intent=getIntent();
        name=intent.getStringExtra("friend_nick");
        tvFriendName=findViewById(R.id.title_friend_name);
        rvChatList=findViewById(R.id.chat_list);
        messageList=new ArrayList<>();
        etMessageContent=findViewById(R.id.edit_message_content);
        btnSendMessage=findViewById(R.id.btn_send_message);
        layout=findViewById(R.id.empty_view);

        tvFriendName.setText(name);

        conversation=Conversation.createSingleConversation(name);

        btnSendMessage.setOnClickListener(this);
        chatListAdapter=new ChatListAdapter(this);

        if (messageList!=null){
            messageList.clear();
        }
        initMessageList();
        if (messageList!=null){
            chatListAdapter.setDate(messageList);
            rvChatList.setLayoutManager(new LinearLayoutManager(this));
            rvChatList.setAdapter(chatListAdapter);
            int itemCount=chatListAdapter.getItemCount()-1;
            rvChatList.smoothScrollToPosition(itemCount);
            chatListAdapter.notifyDataSetChanged();
        }
    }

    private void initMessageList() {
        messageList=conversation.getAllMessage();
        chatListAdapter.setDate(messageList);
        int itemCount=chatListAdapter.getItemCount()-1;
        rvChatList.smoothScrollToPosition(itemCount);
        chatListAdapter.notifyDataSetChanged();
    }


    public void onEvent(MessageEvent event){
        Message message=event.getMessage();
        switch (message.getContentType()){
            case text:
                //文字
                initMessageList();
                chatListAdapter.notifyDataSetChanged();
                break;
            case image:
                //图片
                break;
            case voice:
                //语音
                break;
            case custom:
                //自定义
                break;
            case file:
                //文件
                break;
            case video:
                //视频
                break;
            default:
                break;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_message:
                QMUIKeyboardHelper.hideKeyboard(layout);
                String message=etMessageContent.getText().toString();
                if (message.length()>0){
                    Message message1=JMessageClient.createSingleTextMessage(name, MyApplication.APPKEY,message);
                    JMessageClient.sendMessage(message1);
                    message1.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i==0){
                                initMessageList();
                                Toast.makeText(ShowMessageActivity.this, "消息发送成功！", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ShowMessageActivity.this, "发送失败！"+s, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    etMessageContent.setText("");
                }else {
                    Toast.makeText(ShowMessageActivity.this, "不能发送空消息！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
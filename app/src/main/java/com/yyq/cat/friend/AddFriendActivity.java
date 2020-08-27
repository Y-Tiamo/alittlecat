package com.yyq.cat.friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.yyq.cat.MyApplication;
import com.yyq.cat.R;
import com.yyq.cat.adpter.UserSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class AddFriendActivity extends AppCompatActivity implements UserSearchAdapter.OnUserItemClickListener{

    private SearchView searchView;
    private List<String> nameList = new ArrayList<>();
    private List<String> passList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserSearchAdapter userSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        init();
    }

    private void init() {
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.search_user_list);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchQuery();
            }
        });

        userSearchAdapter = new UserSearchAdapter(this, nameList, passList);
        userSearchAdapter.setOnUserItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userSearchAdapter);
    }
    private void setSearchQuery() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(AddFriendActivity.this, "查询完毕", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    JMessageClient.getUserInfo(newText, new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int i, String s, UserInfo userInfo) {
                            if (i == 0) {
                                nameList.add(userInfo.getUserName());
                                passList.add(userInfo.getNickname());
                            }
                        }
                    });
                }
                return false;
            }
        });
    }

    @Override
    public void addFriend(final int position) {
        final String name = nameList.get(position).trim();
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("提示信息");
        dialog.setMessage("确认向" + name + "发出好友申请吗？");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                ContactManager.sendInvitationRequest(name, MyApplication.APPKEY, "hello", new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String responseMessage) {
                        if (0 == responseCode) {
                            //好友请求请求发送成功
                            Toast.makeText(AddFriendActivity.this, "好友请求请求发送成功" + responseMessage, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            //好友请求发送失败
                            Toast.makeText(AddFriendActivity.this, "好友请求发送失败", Toast.LENGTH_SHORT).show();
                            Log.d("好友请求", name + ":  " + responseMessage);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();

    }

}
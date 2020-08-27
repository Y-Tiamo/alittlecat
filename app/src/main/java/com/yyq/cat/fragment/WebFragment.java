package com.yyq.cat.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyq.cat.MyApplication;
import com.yyq.cat.R;
import com.yyq.cat.adpter.FriendListAdapter;
import com.yyq.cat.adpter.UserSearchAdapter;
import com.yyq.cat.chatroom.ShowMessageActivity;
import com.yyq.cat.friend.AddFriendActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class WebFragment extends Fragment implements
        FriendListAdapter.OnFriendItemClickListener{

    private List<String> friendNickNameList;
    private List<String> friendUserNameList;
    private RecyclerView rvFriendList;
    private Button btnAddFiend;
    private FriendListAdapter friendListAdapter;

    public WebFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JMessageClient.registerEventReceiver(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_web, container, false);
        friendNickNameList=new ArrayList<>();
        friendUserNameList=new ArrayList<>();
        btnAddFiend=view.findViewById(R.id.add_friend);
        rvFriendList = view.findViewById(R.id.friend_recycler_view);

        btnAddFiend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        //初始化好友列表
        initFriendList();
        if (friendNickNameList.size()>0&&friendUserNameList.size()>0){
            rvFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
            friendListAdapter = new FriendListAdapter(getContext(), friendNickNameList, friendUserNameList);
            friendListAdapter.setOnFriendItemClickListener(this);
            rvFriendList.setAdapter(friendListAdapter);
            friendListAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getContext(), "暂时没有好友啊！"+"nick_size:"+friendNickNameList.size()+"   user_size:"+friendUserNameList.size(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void initFriendList() {
        friendNickNameList.clear();
        friendUserNameList.clear();
        for(int i=0;i<1;i++){
            friendNickNameList.add("nick_"+i);
            friendUserNameList.add("user_"+i);
        }
        ContactManager.getFriendList(new GetUserInfoListCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                if (i == 0) {
                    //获取成功
                    if (list.size()>0){
                        for(UserInfo userInfo:list){
                            friendNickNameList.add(userInfo.getNickname());
                            friendUserNameList.add(userInfo.getUserName());
                        }
                        friendListAdapter.notifyDataSetChanged();
                    }else {
                        Log.d("friend_list","暂时没有任何好友");
                    }
                } else {
                    //获取失败
                    Log.d("friend_list", "获取好友失败");
                }
            }
        });
    }

    @Override
    public void showFriendList(int position) {
        String name=friendUserNameList.get(position);
        Intent intent=new Intent(getActivity(), ShowMessageActivity.class);
        intent.putExtra("friend_nick",name);
        startActivity(intent);
    }

    public void onEventMainThread(final ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();
        switch (event.getType()) {
            case invite_received:
                //收到邀请
//                mTv_showAddFriendInfo.append(event.getFromUsername());
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle("Tip");
                dialog.setMessage("收到新的好友请求：" + "\n" + event.getFromUsername());
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactManager.declineInvitation(event.getFromUsername(), MyApplication.APPKEY, "不喜欢", new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(getContext(), "拒绝好友请求成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "拒绝好友请求失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactManager.acceptInvitation(event.getFromUsername(), MyApplication.APPKEY, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    initFriendList();
                                    friendListAdapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "接受好友请求成功！", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getContext(), "接受好友请求失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case invite_accepted:
                //同意
                Toast.makeText(getContext(), fromUsername + "已接受您的好友请求！", Toast.LENGTH_SHORT).show();
                break;
            case invite_declined:
                //拒绝
                Toast.makeText(getContext(), "对方拒绝了你的好友申请！", Toast.LENGTH_SHORT).show();
//                break;
            case contact_deleted:
                //删除
//                mEt_reason.setVisibility(View.GONE);
//                mAccept_invitation.setVisibility(View.GONE);
//                mDeclined_invitation.setVisibility(View.GONE);
//                mTv_showAddFriendInfo.append(intent.getStringExtra("contact_deleted"));
                break;
            case contact_updated_by_dev_api:
                //更新
//                mEt_reason.setVisibility(View.GONE);
//                mAccept_invitation.setVisibility(View.GONE);
//                mDeclined_invitation.setVisibility(View.GONE);
//                mTv_showAddFriendInfo.append(intent.getStringExtra("contact_updated_by_dev_api"));
                break;
            default:
                break;
        }
    }
}
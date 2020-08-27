package com.yyq.cat.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yyq.cat.MyApplication;
import com.yyq.cat.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUserName;
    private EditText etPass;
    private Button btnRegister;
    private TextView tvBackToLogin;

    private CustomVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化界面
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setBg();
    }

    private void setBg(){
        videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login_bg));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    private void initView() {
        etUserName=findViewById(R.id.btn_user_ame);
        etPass=findViewById(R.id.btn_password);
        btnRegister=findViewById(R.id.btn_register);
        tvBackToLogin=findViewById(R.id.tv_back_to_login);

        videoView=findViewById(R.id.video_view);

        btnRegister.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
        setBg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                final String name=etUserName.getText().toString();
                final String pass=etPass.getText().toString();
                RegisterOptionalUserInfo userInfo=new RegisterOptionalUserInfo();
                userInfo.setNickname("用戶："+name);
                JMessageClient.register(name, pass, userInfo, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i==0){
                            Bundle bundle=new Bundle();
                            Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            bundle.putString("user_name",name);
                            bundle.putString("pass",pass);
                            intent.putExtras(bundle);
                            setResult(002,intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_back_to_login:
                finish();
                break;
        }
    }
}
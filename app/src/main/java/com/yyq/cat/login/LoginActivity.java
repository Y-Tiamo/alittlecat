package com.yyq.cat.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyq.cat.ActivitiesManager;
import com.yyq.cat.MainActivity;
import com.yyq.cat.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivHeadImage;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private CustomVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivitiesManager.getInstance().addActivity(this);
        //初始化视图
        initView();
        UserInfo userInfo=JMessageClient.getMyInfo();
        if(userInfo!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setBg();
    }

    /**
     * 初始化视图
     */
    private void initView() {
//        ivHeadImage = findViewById(R.id.iv_user_head_image);
        etUserName = findViewById(R.id.btn_user_ame);
        etPassword = findViewById(R.id.btn_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        videoView=findViewById(R.id.video_view);

        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //check user info
                final String name = etUserName.getText().toString().trim();
                final String pass = etPassword.getText().toString().trim();
                JMessageClient.login(name, pass, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_register:
                //to register
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 001);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name="";
        String pass="";
        if (requestCode == 001 && resultCode == 002) {
            name=data.getStringExtra("user_name");
            pass=data.getStringExtra("pass");
            etUserName.setText(name);
            etPassword.setText(pass);
        }
        JMessageClient.getUserInfo(name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i==0){
                    userInfo.setAddress("用戶 "+s);
                    JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i==0){
                                Toast.makeText(LoginActivity.this, "註冊成功！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
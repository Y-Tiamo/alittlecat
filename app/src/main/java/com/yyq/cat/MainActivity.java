package com.yyq.cat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.yyq.cat.activity.FootPointActivity;
import com.yyq.cat.activity.PersonCenterActivity;
import com.yyq.cat.activity.ShareActivity;
import com.yyq.cat.bean.WeatherInfoBean;
import com.yyq.cat.fragment.DrawFragment;
import com.yyq.cat.fragment.JournalFragment;
import com.yyq.cat.fragment.WebFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AMapLocationListener {
    private ViewPager viewPager;//切换
    private List<View> viewList;//视图列表
    private ImageView ivHeadImage;//头像
    private ImageView ivToolBarLeftImage;
    private TextView tvSign;//簽名部分
    private Handler handler;
    private Thread thread;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    int index = 0;

    private LinearLayout layout1, layout2, layout3, layout4;//测出菜单底部菜单

    public AMapLocationClient mapLocationClient;//声明AMapLocationClient类对象
    public AMapLocationClientOption mLocationOption;//声明AMapLocationClientOption对象

    private TextView tvChengshi;
    private TextView tvWendu;

    private JournalFragment journalFragment=null;
    private WebFragment webFragment=null;
    private DrawFragment drawFragment=null;
    private BottomNavigationView bottomNavi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        JMessageClient.registerEventReceiver(this);
        ActivitiesManager.getInstance().addActivity(this);
        //初始化界面
        initLayout();

    }

    @Override
    protected void onStart() {
        super.onStart();
        onPermissions();//检察是否具有权限
//        getLocation();//获取定位信息
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }

    public void onEvent(ContactNotifyEvent event){
        String reason =event.getReason();
        String fromUserName=event.getFromUsername();
        String appkey=event.getfromUserAppKey();
        switch (event.getType()){
            case invite_received://收到好友邀请
                Log.d("好友请求",fromUserName);
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }

    /**
     * 初始化界面
     */
    private void initLayout() {
        navigationView = findViewById(R.id.left_menu);
        ivHeadImage = findViewById(R.id.head_image);
        ivToolBarLeftImage = findViewById(R.id.toolbar_left_image);
        drawerLayout = findViewById(R.id.drawer_layout);
        tvChengshi = findViewById(R.id.chengshi);
        tvWendu = findViewById(R.id.wendu);

        tvSign=findViewById(R.id.sign);
        UserInfo userInfo=JMessageClient.getMyInfo();
        tvSign.setText(userInfo.getUserName());

        bottomNavi = findViewById(R.id.navi_buttom);
        bottomNavi.setSelectedItemId(R.id.journal);
        addFragment(R.id.journal);
        //OnNavigationItemSelectedListener
        bottomNavi.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        layout1 = findViewById(R.id.night_mode);
        layout2 = findViewById(R.id.set);
        layout3 = findViewById(R.id.date_count);
        layout4 = findViewById(R.id.finish);

        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout4.setOnClickListener(this);

        ivToolBarLeftImage.setOnClickListener(this);

        Glide.with(this).load(R.drawable.view_page1).circleCrop().into(ivHeadImage);
        Glide.with(this).load(R.drawable.view_page1).circleCrop().into(ivToolBarLeftImage);
        //初始化轮播
        initViewPager();

        navigationView.setNavigationItemSelectedListener(this);
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    if (index < 4) {
                        viewPager.setCurrentItem(index++);
                    } else {
                        index = 0;
                        viewPager.setCurrentItem(index++);
                    }
                }
            }
        };
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.journal:
                    viewPager.setVisibility(View.VISIBLE);
                    removeAllFragment();
                    addFragment(R.id.journal);
                    menuItem.setChecked(true);
                    break;
                case R.id.web:
                    viewPager.setVisibility(View.VISIBLE);
                    removeAllFragment();
                    addFragment(R.id.web);
                    menuItem.setChecked(true);
                    break;
                case R.id.draw:
                    viewPager.setVisibility(View.GONE);
                    removeAllFragment();
                    addFragment(R.id.draw);
                    menuItem.setChecked(true);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     * 移除所有fragment
     */
    public void removeAllFragment(){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        if (journalFragment!=null){
            transaction.remove(journalFragment);
        }
        if (webFragment!=null){
            transaction.remove(webFragment);
        }
        if (drawFragment!=null){
            transaction.remove(drawFragment);
        }
        transaction.commit();
    }

    /**
     * 根据选择添加fragment
     * @param checkId
     */
    public void addFragment(int checkId){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        switch (checkId){
            case R.id.journal:
                journalFragment=new JournalFragment();
                transaction.add(R.id.frame_layout,journalFragment);
                break;
            case R.id.web:
                webFragment=new WebFragment();
                transaction.add(R.id.frame_layout,webFragment);
                break;
            case R.id.draw:
                drawFragment=new DrawFragment();
                transaction.add(R.id.frame_layout,drawFragment);
                break;
        }
        transaction.commit();
    }

    /**
     * 动态申请权限
     */
    private void onPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * 权限申请
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "用户已同意定位授权！", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("提示信息")
                            .setMessage("当前应用缺少必要权限，该功能暂时无法使用，如若需要，单击【确定】按钮前往设置中心进行权限授权。")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "未授权！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package" + getPackageName()));
                                    startActivity(intent);
                                }
                            }).show();
                }
        }
    }

    /**
     * 获取当前位置信息
     */
    private void getLocation() {
        mapLocationClient = new AMapLocationClient(getApplicationContext());//初始化定位
        mapLocationClient.setLocationListener(this);

        setLocationOption();//设置Option
        mapLocationClient.stopLocation();//停止定位
        mapLocationClient.startLocation();//开启定位
    }

    /**
     * 设置Option
     */
    private void setLocationOption() {
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//定位模式
        //获取一次定位结果：
        // 该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mapLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 侧滑菜单的底部菜单栏的单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.night_mode:
                Toast.makeText(this, "夜间", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.finish://退出
                JMessageClient.logout();
                ActivitiesManager.getInstance().exit();
                finish();
                break;
            case R.id.btn_to_web:
                Toast.makeText(this, "动态", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_left_image:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    /**
     * 侧滑菜单的菜单项的单击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.nav_person_center://个人中心
                intent = new Intent(MainActivity.this, PersonCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_foot_point://运动足迹
                intent = new Intent(MainActivity.this, FootPointActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share://分享
                intent = new Intent(MainActivity.this, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item4:
                Toast.makeText(this, "点击了菜单四", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item5:
                Toast.makeText(this, "点击了菜单五", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item6:
                Toast.makeText(this, "点击了菜单六", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item7:
                Toast.makeText(this, "点击了菜单七", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item8:
                Toast.makeText(this, "点击了菜单八", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item9:
                Toast.makeText(this, "点击了菜单九", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 初始化轮播
     */
    private void initViewPager() {
        viewPager = findViewById(R.id.view_page);
        viewList = new ArrayList<>();//轮播图片列表
        //初始化轮播图片列表
        addViews();
        viewPager.setAdapter(adapter);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 添加图片
     */
    private void addViews() {
        LayoutInflater inflater = getLayoutInflater().from(this);
        viewList.add(inflater.inflate(R.layout.layout_lunbo1, null));
        viewList.add(inflater.inflate(R.layout.layout_lunbo2, null));
        viewList.add(inflater.inflate(R.layout.layout_lunbo3, null));
        viewList.add(inflater.inflate(R.layout.layout_lunbo4, null));
    }

    /**
     * ViewPager适配器
     */
    PagerAdapter adapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(viewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    };

    /**
     * 获取定位信息并显示
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                tvChengshi.setText(aMapLocation.getDistrict());

                getWeatherInfoByName(aMapLocation.getCity());
//                getWeatherInfoByLL(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                mapLocationClient.stopLocation();
            } else {
                Log.e("AmapError", "location Error,ErrorCode:" + aMapLocation.getErrorCode()
                        + ", errorInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 天气信息
     * https://api.seniverse.com/v3/weather/now.json?key=S_WbNbNwfb7HKQzOk&location=%E5%8C%97%E4%BA%AC&language=zh-Hans&unit=c
     */
    public void getWeatherInfoByName(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                Request request = new Request.Builder()
                        .url("https://api.seniverse.com/v3/weather/now.json?key=S_WbNbNwfb7HKQzOk&location="
                                + s + "&language=zh-Hans&unit=c")
                        .build();
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    Gson gson = new Gson();
                    WeatherInfoBean bean = gson.fromJson(response.body().string(), WeatherInfoBean.class);
                    tvWendu.setText(bean.getResults().get(0).getNow().getTemperature() + "°");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }

    /**
     * 天气信息
     * https://api.seniverse.com/v3/weather/now.json?key=S_WbNbNwfb7HKQzOk&location=%E5%8C%97%E4%BA%AC&language=zh-Hans&unit=c
     */
    public void getWeatherInfoByLL(final double la, final double lo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                Request request = new Request.Builder()
                        .url("https://api.seniverse.com/v3/weather/now.json?key=S_WbNbNwfb7HKQzOk&location="
                                + la + ":" + lo + "&language=zh-Hans&unit=c")
                        .build();
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    Gson gson = new Gson();
                    WeatherInfoBean bean = gson.fromJson(response.body().string(), WeatherInfoBean.class);
                    tvWendu.setText(bean.getResults().get(0).getNow().getTemperature() + "°");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();

    }
}
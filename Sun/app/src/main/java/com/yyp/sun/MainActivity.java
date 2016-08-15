package com.yyp.sun;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyp.sun.config.SunInfo;
import com.yyp.sun.ui.app.AppAboutActivity;
import com.yyp.sun.ui.mood.MoodFragment;
import com.yyp.sun.ui.news.NewsFragment;
import com.yyp.sun.ui.test.TestFragment;
import com.yyp.sun.ui.user.LoginActivity;
import com.yyp.sun.ui.user.ProfileActivity;
import com.yyp.sun.ui.user.data.UserInfo;
import com.yyp.sun.util.AuthUtil;
import com.yyp.sun.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;
    @BindView(R.id.navigationView)
    NavigationView navigationView;

    private View header;
    private SimpleDraweeView headerView;
    private TextView userName;
    private SimpleDraweeView userSex;

    private String[] tabTitle = {"测试", "资讯", "心情"};
    private ActionBarDrawerToggle drawerToggle;

    private UserInfo user;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        c = MainActivity.this;
        user = AuthUtil.getCurrentUser();

        initToolbar();
        initNavigation();
        initViewPager();
        initTabLayout();
    }

    @OnClick(R.id.main_fab)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fab:
                ToastUtil.showToast(c, "fab");
                break;
        }
    }

    /**
     * 初始化 toolbar
     */
    public void initToolbar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        // drawer开关
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    /**
     * 初始化侧栏
     */
    public void initNavigation() {
        // 获取侧边栏的头部布局
        header = navigationView.getHeaderView(0);
        headerView = (SimpleDraweeView) header.findViewById(R.id.header_view);
        userName = (TextView) header.findViewById(R.id.header_username);
        userSex = (SimpleDraweeView) header.findViewById(R.id.header_sex);

        // 初始化头像、用户名、性别
        if(AuthUtil.isLogin()){
            userName.setText(user.getNickName());
            headerView.setImageURI(Uri.parse(user.getAvatarUrl()));
            if(user.getSex().equals("男")){
                userSex.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.sex_boy));
            }else{
                userSex.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.sex_girl));
            }
        }else{
            userName.setText("请登录");
            headerView.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.avatar));
            userSex.setImageURI(Uri.parse("res://com.yyp.sun/" + R.drawable.sex_boy));
        }
        // 点击侧栏的头部
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!AuthUtil.isLogin()){
                    Intent goLogin = new Intent(c, LoginActivity.class);
                    startActivity(goLogin);
                    finish();
                }else {
                    Intent goProfile = new Intent(c, ProfileActivity.class);
                    startActivityForResult(goProfile, SunInfo.CODE_IN_PEOFILE);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_app_about:
                        Intent goAppAbout = new Intent(c, AppAboutActivity.class);
                        startActivity(goAppAbout);
                        break;
                    case R.id.menu_app_developer:
                        
                        break;
                    default:
                        break;
                }

                item.setChecked(true);
                // 进入后关闭侧边栏
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /**
     * 初始化 tab 选项卡
     */
    public void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitle[2]));
        // tab 和 viewpager 关联起来
        tabLayout.setupWithViewPager(viewPager);

        //否则无法显示title
        tabLayout.getTabAt(0).setText(tabTitle[0]);
        tabLayout.getTabAt(1).setText(tabTitle[1]);
        tabLayout.getTabAt(2).setText(tabTitle[2]);
    }

    /**
     * 初始化 viewpager
     */
    public void initViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new TestFragment());
        pagerAdapter.addFragment(new NewsFragment());
        pagerAdapter.addFragment(new MoodFragment());
        // 设置页面缓存为2
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下返回键，先关闭侧边栏
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawers();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 再恢复界面的时候，恢复这个开关的状态
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * viewpager 适配器
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        /**
         * 添加一个Fragment
         *
         * @param fragment
         */
        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果用户信息改变，更新侧栏头部
        if (data != null && data.getExtras().getBoolean("isUpdated")){
            user = AuthUtil.getCurrentUser();
            initNavigation();
        }
    }
}

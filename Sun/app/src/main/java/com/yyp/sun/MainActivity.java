package com.yyp.sun;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyp.sun.ui.mood.MoodFragment;
import com.yyp.sun.ui.news.NewsFragment;
import com.yyp.sun.ui.test.TestFragment;
import com.yyp.sun.ui.user.LoginActivity;
import com.yyp.sun.util.AuthUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initNavigation();
        initViewPager();
        initTabLayout();
    }

    @OnClick(R.id.main_fab)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fab:
                Toast.makeText(getApplicationContext(), "fab", Toast.LENGTH_SHORT).show();
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
            userName.setText(AuthUtil.getCurrentUser().getUsername());
            headerView.setImageURI(Uri.parse(AuthUtil.getCurrentUser().getAvatarUrl()));
            if(AuthUtil.getCurrentUser().getSex().equals("男")){
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
                    Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goLogin);
                }else {
                    Intent goLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goLogin);
                }
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 再恢复界面的时候，恢复这个开关的状态
        drawerToggle.syncState();
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
}

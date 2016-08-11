package com.yyp.sun;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyp.sun.mood.MoodFragment;
import com.yyp.sun.news.NewsFragment;
import com.yyp.sun.test.TestFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @ViewById(R.id.main_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.main_tabLayout)
    TabLayout tabLayout;
    @ViewById(R.id.main_viewpager)
    ViewPager viewPager;
    @ViewById(R.id.navigationView)
    NavigationView navigationView;

    private View header;
    private SimpleDraweeView headerView;
    private TextView userName;

    private String[] tabTitle= {"测试","资讯","心情"};
    private ActionBar ab;
    private ActionBarDrawerToggle drawerToggle;

    @AfterViews
    void afterView(){
        initData();

        initToolbar();
        initNavigation();
        initViewPager();
        initTabLayout();
    }

    @Click({R.id.main_fab})
    void handleClick(View v){
        switch (v.getId()){
            case R.id.main_fab:
                Toast.makeText(getApplicationContext(), "fab", Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }

    public void initData(){

    }

    /**
     * 初始化 toolbar
     */
    public void initToolbar(){
        //ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        getActionBar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    public void initNavigation(){
        header = navigationView.getHeaderView(0);
        headerView = (SimpleDraweeView) header.findViewById(R.id.header_view);
        userName = (TextView) header.findViewById(R.id.header_username);

        userName.setText("请登录");
       /* Uri uri = Uri.parse("res://com.yyp.sun/" + R.drawable.avatar);
        headerView.setImageURI(uri);*/
    }

    /**
     * 初始化 tab 选项卡
     */
    public void initTabLayout(){
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
    public void initViewPager(){
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new TestFragment());
        pagerAdapter.addFragment(new NewsFragment());
        pagerAdapter.addFragment(new MoodFragment());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下并向左滑动，关闭侧边栏
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
        // Pass any configuration change to the drawer toggles
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

package com.yyp.socket.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyp.socket.R;
import com.yyp.socket.bean.getmusiclist.GetMusicListPkt;
import com.yyp.socket.bean.getmusiclist.GetMusicListReplyPkt;
import com.yyp.socket.bean.header.PktHeader;
import com.yyp.socket.bean.logout.LogoutPkt;
import com.yyp.socket.bean.music.Music;
import com.yyp.socket.bean.playstartstop.PlayStartStopPkt;
import com.yyp.socket.bean.playstartstop.PlayStartStopReplyPkt;
import com.yyp.socket.config.PktType;
import com.yyp.socket.thread.SocketThread;
import com.yyp.socket.utils.ToastUtils;
import com.yyp.socket.view.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicPlayActivity extends AppCompatActivity {

    ActionBar actionBar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.music_play_tips)
    TextView musicPlayTips;

    private String username;

    @BindView(R.id.music_play_list_view)
    ListView musicPlayListView;
    List<Music> data = new ArrayList<>();
    MusicAdapter musicAdapter;

    TextView musicName;
    ImageView startStop;
    Map<String, Integer> playMap = new HashMap<>(); //每条音乐的播放
    int[] playImage = {R.drawable.music_play_start, R.drawable.music_play_stop};

    SocketThread musicThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        ButterKnife.bind(this);

        initActionBar();
        initListView();

        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
        }

        startSocket();
    }

    public void initActionBar() {
        // ToolBar
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // 设置标题
        toolbarTitle.setText("音乐台");
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 去掉 ActionBar 自带标题
            actionBar.setTitle(null);
        }
    }

    public void initListView() {
        if(data.size() > 0){
            musicPlayTips.setVisibility(View.GONE);
            musicPlayListView.setVisibility(View.VISIBLE);
        }else{
            musicPlayTips.setVisibility(View.VISIBLE);
            musicPlayListView.setVisibility(View.GONE);
        }
        musicAdapter = new MusicAdapter(data, this);
        musicPlayListView.setAdapter(musicAdapter);
        musicPlayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startStop = (ImageView) view.findViewById(R.id.music_item_music_start_stop);
                musicName = (TextView) view.findViewById(R.id.music_item_music_name);

                playStartStop(musicName.getText().toString().trim());
            }
        });
    }

    public void startSocket() {
        musicThread = new SocketThread(outHandler, inHandler);
        musicThread.start();
    }

    public Handler outHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.showToast(MusicPlayActivity.this, "网络错误");
                    break;
            }
        }
    };

    public Handler inHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PktHeader header = new PktHeader((byte[]) msg.obj);
            switch (header.getPktType()) {
                case PktType.PKT_GET_MUSIC_LIST_REPLY:
                    GetMusicListReplyPkt pkt = new GetMusicListReplyPkt((byte[]) msg.obj);
                    String name = getFileNameNoEx(pkt.getMusicName());
                    musicAdapter.addItem(new Music(name));
                    playMap.put(name, 0);
                    if(data.size() > 0){
                        musicPlayTips.setVisibility(View.GONE);
                        musicPlayListView.setVisibility(View.VISIBLE);
                    }else{
                        musicPlayTips.setVisibility(View.VISIBLE);
                        musicPlayListView.setVisibility(View.GONE);
                    }
                    break;
                case PktType.PKT_PLAY_START_STOP_REPLY:
                    PlayStartStopReplyPkt replyPkt = new PlayStartStopReplyPkt((byte[]) msg.obj);
                    if (replyPkt.getRet() == PktType.PLAY_START_SUCCESS) {
                        playMap.put(musicName.getText().toString().trim(), 1);
                        startStop.setImageResource(playImage[1]);
                        ToastUtils.showToast(MusicPlayActivity.this, "播放成功！");
                    }
                    if (replyPkt.getRet() == PktType.PLAY_START_FAIL) {
                        ToastUtils.showToast(MusicPlayActivity.this, "播放失败！");
                    }
                    if (replyPkt.getRet() == PktType.PLAY_STOP_SUCCESS) {
                        playMap.put(musicName.getText().toString().trim(), 0);
                        startStop.setImageResource(playImage[0]);
                        ToastUtils.showToast(MusicPlayActivity.this, "停止成功！");
                    }
                    if (replyPkt.getRet() == PktType.PLAY_STOP_FAIL) {
                        ToastUtils.showToast(MusicPlayActivity.this, "停止失败！");
                    }
                    break;
            }
        }
    };

    public void getMusicList() {
        // 构建包
        GetMusicListPkt pkt = new GetMusicListPkt();
        pkt.setHeader(new PktHeader(4, PktType.PKT_GET_MUSIC_LIST));

        musicThread.send(pkt.getBuf());
    }

    /**
     * 获取不带文件类型的文件名
     * @param filename
     * @return
     */
    public String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public void playStartStop(String name) {
        for(Map.Entry<String, Integer> entry : playMap.entrySet()){
            if(entry.getKey().equals(name)){
                if(entry.getValue() == 0){
                    if(!isPlaying()){
                        // 构建开始播放包
                        PlayStartStopPkt pkt = new PlayStartStopPkt();
                        pkt.setHeader(new PktHeader(4+4+30, PktType.PKT_PLAY_START_STOP));
                        pkt.setType(PktType.PLAY_START);
                        pkt.setMusicName(name);
                        musicThread.send(pkt.getBuf());
                    }else{
                        ToastUtils.showToast(getApplicationContext(), "不允许同时播放多首音乐");
                    }
                }else{
                    // 构建停止播放包
                    PlayStartStopPkt pkt = new PlayStartStopPkt();
                    pkt.setHeader(new PktHeader(4+4+30, PktType.PKT_PLAY_START_STOP));
                    pkt.setType(PktType.PLAY_STOP);
                    pkt.setMusicName(name);
                    musicThread.send(pkt.getBuf());
                }
            }
        }
    }

    /**
     * 是否有音乐在播放
     * @return
     */
    public boolean isPlaying(){
        for(Map.Entry<String, Integer> entry : playMap.entrySet()){
            if(entry.getValue() == 1){
                return true;
            }
        }
        return false;
    }

    public void logout() {
        // 构建包
        LogoutPkt pkt = new LogoutPkt();
        pkt.setHeader(new PktHeader(4+30, PktType.PKT_LOGOUT));
        pkt.setUsername(username);

        musicThread.send(pkt.getBuf());
        musicThread.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_play_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.music_play_menu_get_music_list:
                if(!isPlaying()){
                    //每获取一次必须清楚原有数据
                    musicAdapter.clearAll();
                    playMap.clear();
                    getMusicList();
                }else {
                    ToastUtils.showToast(getApplicationContext(), "请先关闭当前播放的音乐");
                }
                break;
            case R.id.music_play_menu_logout:
                logout();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicThread.close();
    }
}

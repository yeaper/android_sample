// Generated code from Butter Knife. Do not modify!
package com.yyp.socket.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.yyp.socket.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MusicPlayActivity_ViewBinding<T extends MusicPlayActivity> implements Unbinder {
  protected T target;

  @UiThread
  public MusicPlayActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.musicPlayTips = Utils.findRequiredViewAsType(source, R.id.music_play_tips, "field 'musicPlayTips'", TextView.class);
    target.musicPlayListView = Utils.findRequiredViewAsType(source, R.id.music_play_list_view, "field 'musicPlayListView'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbarTitle = null;
    target.toolbar = null;
    target.musicPlayTips = null;
    target.musicPlayListView = null;

    this.target = null;
  }
}

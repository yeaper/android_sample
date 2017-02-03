// Generated code from Butter Knife. Do not modify!
package com.yyp.socket;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> implements Unbinder {
  protected T target;

  private View view2131492978;

  @UiThread
  public MainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.mainUsername = Utils.findRequiredViewAsType(source, R.id.main_username, "field 'mainUsername'", EditText.class);
    target.mainPassword = Utils.findRequiredViewAsType(source, R.id.main_password, "field 'mainPassword'", EditText.class);
    view = Utils.findRequiredView(source, R.id.main_login, "method 'onClick'");
    view2131492978 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mainUsername = null;
    target.mainPassword = null;

    view2131492978.setOnClickListener(null);
    view2131492978 = null;

    this.target = null;
  }
}

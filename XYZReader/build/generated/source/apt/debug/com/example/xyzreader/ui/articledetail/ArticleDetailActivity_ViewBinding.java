// Generated code from Butter Knife. Do not modify!
package com.example.xyzreader.ui.articledetail;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.xyzreader.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ArticleDetailActivity_ViewBinding<T extends ArticleDetailActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ArticleDetailActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.mPager = Utils.findRequiredViewAsType(source, R.id.pager, "field 'mPager'", ViewPager.class);
    target.mUpButton = Utils.findRequiredView(source, R.id.action_up, "field 'mUpButton'");
    target.mUpButtonContainer = Utils.findRequiredView(source, R.id.up_container, "field 'mUpButtonContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mPager = null;
    target.mUpButton = null;
    target.mUpButtonContainer = null;

    this.target = null;
  }
}

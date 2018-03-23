// Generated code from Butter Knife. Do not modify!
package com.example.xyzreader.ui.articledetail;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.xyzreader.R;
import com.example.xyzreader.ui.customviews.DrawInsetsFrameLayout;
import com.example.xyzreader.ui.customviews.ObservableScrollView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ArticleDetailFragment_ViewBinding<T extends ArticleDetailFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ArticleDetailFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.mScrollView = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'mScrollView'", ObservableScrollView.class);
    target.mDrawInsetsFrameLayout = Utils.findRequiredViewAsType(source, R.id.draw_insets_frame_layout, "field 'mDrawInsetsFrameLayout'", DrawInsetsFrameLayout.class);
    target.mPhotoContainerView = Utils.findRequiredView(source, R.id.photo_container, "field 'mPhotoContainerView'");
    target.mPhotoView = Utils.findRequiredViewAsType(source, R.id.photo, "field 'mPhotoView'", ImageView.class);
    target.actionButton = Utils.findRequiredViewAsType(source, R.id.share_fab, "field 'actionButton'", FloatingActionButton.class);
    target.titleView = Utils.findRequiredViewAsType(source, R.id.article_title, "field 'titleView'", TextView.class);
    target.bylineView = Utils.findRequiredViewAsType(source, R.id.article_byline, "field 'bylineView'", TextView.class);
    target.bodyView = Utils.findRequiredViewAsType(source, R.id.article_body, "field 'bodyView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mScrollView = null;
    target.mDrawInsetsFrameLayout = null;
    target.mPhotoContainerView = null;
    target.mPhotoView = null;
    target.actionButton = null;
    target.titleView = null;
    target.bylineView = null;
    target.bodyView = null;

    this.target = null;
  }
}

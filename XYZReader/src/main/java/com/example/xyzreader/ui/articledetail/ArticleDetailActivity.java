package com.example.xyzreader.ui.articledetail;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.article.ArticleListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ARTICLES = "ARTICLES";
    public static final String EXTRA_ARTICLES_ID = "ARTICLES_ID";
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.action_up)
    View mUpButton;
    @BindView(R.id.up_container)
    View mUpButtonContainer;
    private int mStartId;
    private int mSelectedItemId;
    private int mSelectedItemUpButtonFloor = Integer.MAX_VALUE;
    private int mTopInset;
    private MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            postponeEnterTransition();
        }
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mStartId = getIntent().getIntExtra(EXTRA_ARTICLES_ID, 0);
            mSelectedItemId = mStartId;
        }

        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageMargin((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                mUpButton.animate()
                        .alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
                        .setDuration(300);
            }

            @Override
            public void onPageSelected(int position) {
//                if (mCursor != null) {
//                    mCursor.moveToPosition(position);
//                }
//                mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
                updateUpButtonPosition();
            }

        });
        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mPager.setCurrentItem(mStartId);
        scheduleStartPostponedTransition(mPager);


        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                // mStartId = ItemsContract.Items.getItemId(getIntent().getData());
                mStartId = getIntent().getIntExtra(EXTRA_ARTICLES_ID, 0);
                //getIntent().getParcelableArrayListExtra(EXTRA_ARTICLES);
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mUpButtonContainer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    view.onApplyWindowInsets(windowInsets);
                    mTopInset = windowInsets.getSystemWindowInsetTop();
                    mUpButtonContainer.setTranslationY(mTopInset);
                    updateUpButtonPosition();
                    return windowInsets;
                }
            });
        }


    }


    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }

    public void onUpButtonFloorChanged(long itemId, ArticleDetailFragment fragment) {
        if (itemId == mSelectedItemId) {
            mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
            updateUpButtonPosition();
        }
    }

    private void updateUpButtonPosition() {
        int upButtonNormalBottom = mTopInset + mUpButton.getHeight();
        mUpButton.setTranslationY(Math.min(mSelectedItemUpButtonFloor - upButtonNormalBottom, 0));

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            ArticleDetailFragment fragment = (ArticleDetailFragment) object;
            if (fragment != null) {
                mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
                updateUpButtonPosition();
            }
        }

        @Override
        public Fragment getItem(int position) {
            ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(
                    ArticleListActivity.mArticleList.get(position));
            return articleDetailFragment;
        }

        @Override
        public int getCount() {
            return ArticleListActivity.mArticleList.size();
            //return (mCursor != null) ? mCursor.getCount() : 0;
        }
    }
}

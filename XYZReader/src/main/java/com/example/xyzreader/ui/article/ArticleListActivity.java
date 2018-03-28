package com.example.xyzreader.ui.article;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.adapters.ArticleAdapter;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.data.source.ArticlesRepository;
import com.example.xyzreader.data.source.local.ArticleLocalDataSoure;
import com.example.xyzreader.data.source.remote.ArticleRemoteDataSource;
import com.example.xyzreader.ui.articledetail.ArticleDetailActivity;
import com.example.xyzreader.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements ArticleContract.View {

    private static final String TAG = ArticleListActivity.class.toString();
    private static final String BUNDLE_ARTICLE = "BUNDLE_ARTICLE";
    public static List<Article> mArticleList = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    ArticleAdapter.ArticleItemListener mArticleItemListener = new ArticleAdapter.ArticleItemListener() {
        @Override
        public void onArticleClick(@NonNull int position, @NonNull ImageView imageView) {

            Intent intentDetailActivity = new Intent(getApplicationContext(), ArticleDetailActivity.class);
            intentDetailActivity.putExtra(ArticleDetailActivity.EXTRA_ARTICLES_ID, position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startWithTransition(intentDetailActivity, imageView);
            } else {
                startActivity(intentDetailActivity);
            }
        }

    };
    private ArticleAdapter mArticleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isAppVisible = false;
    private ArticlePresenter mArticlePresenter;
    private boolean mIsRefreshing = false;
    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        initRecycleView();
        //Creating presenter  this can be improve with dagger(injection)
        mArticlePresenter = new ArticlePresenter(
                ArticlesRepository.getInstance(
                        ArticleRemoteDataSource.getNewInstance(new AppExecutors()),
                        ArticleLocalDataSoure.getNewInstance(getContentResolver(), new AppExecutors())),
                this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticlePresenter.loadArticle(true);
            }
        });
        if (savedInstanceState == null) {
            mArticlePresenter.start();
        } else {
            ArrayList<Article> parcelableArrayList = savedInstanceState
                    .getParcelableArrayList(BUNDLE_ARTICLE);
            mArticleAdapter.swapArticlesData(parcelableArrayList);

        }


    }

    private void refresh() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_ARTICLE, (ArrayList<? extends Parcelable>) mArticleList);
    }

    //Set adapter and layoutManager to Recycleview
    private void initRecycleView() {
        mArticleAdapter = new ArticleAdapter(mArticleItemListener);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mArticleAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAppVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAppVisible = false;
        unregisterReceiver(mRefreshingReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startWithTransition(Intent intent, View view) {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this,
                view,
                ViewCompat.getTransitionName(view)).toBundle();
        startActivity(intent, bundle);

    }

    private void updateRefreshingUI() {
//        mSwipeRefreshLayout.setRefreshing(
//                mIsRefreshing
//        );
    }


    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showArticle(List<Article> articles) {
        Log.d("ShowArticle", articles.toString());
        mArticleList = articles;
        mArticleAdapter.swapArticlesData(articles);
        setLoadingIndicator(false);
    }

    @Override
    public void showArticleDetailsUi(String articleId) {

    }

    @Override
    public void showLoadingArticleError() {

    }

    @Override
    public void showNoArticle() {

    }

    @Override
    public boolean isActive() {

        return isAppVisible;
    }


}

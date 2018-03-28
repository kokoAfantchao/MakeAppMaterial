package com.example.xyzreader.ui.article;

import android.support.annotation.NonNull;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.source.ArticlesDataSource;
import com.example.xyzreader.data.source.ArticlesRepository;

import java.util.List;

/**
 * Created by nestor on 2/28/18.
 */

public class ArticlePresenter implements ArticleContract.Presenter {

    private final ArticlesRepository mArticlesRepository;
    private final ArticleContract.View mArticlsView;
    private boolean fistLoad = true;

    public ArticlePresenter(@NonNull ArticlesRepository articlesRepository,
                            @NonNull ArticleContract.View articlesView) {
        mArticlesRepository = articlesRepository;
        mArticlsView = articlesView;
    }

    @Override
    public void start() {
        loadArticle(true);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadArticle(boolean forceUpdate) {
        loadArticle(forceUpdate || fistLoad, true);
        fistLoad = false;

    }

    private void loadArticle(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mArticlsView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mArticlesRepository.refreshArticles();
        }

        mArticlesRepository.getArticles(new ArticlesDataSource.LoadArticlesCallback() {
            @Override
            public void onArticlesLoaded(List<Article> Articles) {
                if (!mArticlsView.isActive()) return;
                mArticlsView.showArticle(Articles);

                if (showLoadingUI) mArticlsView.setLoadingIndicator(false);

            }

            @Override
            public void onDataNotAvailable() {
                mArticlsView.showNoArticle();

            }
        });

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        // EspressoIdlingResource.increment(); // App is busy until further notice

    }

    @Override
    public void openArticleDetails(@NonNull Article requestedArticle) {

    }

}


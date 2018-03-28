package com.example.xyzreader.ui.articledetail;

import android.support.annotation.NonNull;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.source.ArticlesDataSource;
import com.example.xyzreader.data.source.ArticlesRepository;

/**
 * Created by nestor on 3/27/18.
 */

public class DetailFragmentPresenter implements ArticleDetailContract.Presenter {

    private final ArticlesRepository mArticlesRepository;
    private final ArticleDetailContract.View mArticleDetailView;


    public DetailFragmentPresenter(@NonNull ArticlesRepository articlesRepository,
                                   @NonNull ArticleDetailContract.View view) {
        mArticlesRepository = articlesRepository;
        mArticleDetailView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void getArticleContent(final String articleId) {
        mArticlesRepository.getArticleWithFullContent(articleId, new ArticlesDataSource.GetArticleCallback() {
            @Override
            public void onArticleLoaded(Article Article) {
                mArticleDetailView.setLoadingIndicator(false);
                mArticleDetailView.showArticleContent(Article.getBODY());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void loadArticle(boolean forceUpdate) {

    }
}

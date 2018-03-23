package com.example.xyzreader.ui.article;

import android.support.annotation.NonNull;

import com.example.xyzreader.BasePresenter;
import com.example.xyzreader.BaseView;
import com.example.xyzreader.data.Article;

import java.util.List;

/**
 * Created by nestor on 2/28/18.
 */

public interface ArticleContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showArticle(List<Article> articles);

        void showArticleDetailsUi(String articleId);

        void showLoadingArticleError();

        void showNoArticle();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadArticle(boolean forceUpdate);

        void openArticleDetails(@NonNull Article requestedArticle);
    }
}

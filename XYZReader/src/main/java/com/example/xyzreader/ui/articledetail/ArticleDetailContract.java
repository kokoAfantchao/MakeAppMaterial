package com.example.xyzreader.ui.articledetail;

import com.example.xyzreader.BasePresenter;
import com.example.xyzreader.BaseView;
import com.example.xyzreader.ui.article.ArticleContract;

/**
 * Created by nestor on 3/27/18.
 */

public interface ArticleDetailContract {
    interface View extends BaseView<ArticleContract.Presenter> {

        void setLoadingIndicator(boolean active);

        void showArticleContent(String Content);

    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void getArticleContent(String ArticleId);

        void loadArticle(boolean forceUpdate);


    }

}

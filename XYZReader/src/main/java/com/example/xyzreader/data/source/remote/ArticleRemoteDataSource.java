package com.example.xyzreader.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.source.ArticlesDataSource;
import com.example.xyzreader.utils.AppExecutors;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by nestor on 2/28/18.
 */

public class ArticleRemoteDataSource implements ArticlesDataSource {

   private static final String BASE_URL = "https://go.udacity.com/";
   private static ArticleRemoteDataSource mRemoteDataSource;
   private Retrofit retrofit;
   private ArticleService mArticleService;
   private static AppExecutors mAppExecutors;

   private ArticleRemoteDataSource() {
       retrofitBuilder();
   }



    public static  ArticleRemoteDataSource getNewInstance(AppExecutors appExecutors){
        if (mRemoteDataSource != null ) return mRemoteDataSource ;
         mRemoteDataSource = new ArticleRemoteDataSource();
         mAppExecutors = appExecutors;
         return  mRemoteDataSource;
    }


    private void retrofitBuilder(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      mArticleService = retrofit.create(ArticleService.class);
    }

    @Override
    public void getArticles(@NonNull final LoadArticlesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    Call<List<Article>> listCall = mArticleService.listRepos();
                    final Response<List<Article>> execute = listCall.execute();
                    final List<Article> articleList  = execute.body();

                     mAppExecutors.mainThread().execute(new Runnable() {
                         @Override
                         public void run() {
                             if (!articleList.isEmpty()){
                                 callback.onArticlesLoaded(articleList);
                             }else {
                                 callback.onDataNotAvailable();
                             }
                         }
                     });
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                }

            }
        };
        mAppExecutors.networkIO().execute(runnable);


    }

    @Override
    public void getArticle(@NonNull String ArticleId, @NonNull GetArticleCallback callback) {

    }

    @Override
    public void saveArticle(@NonNull Article Article) {

    }

    @Override
    public void saveArticle(@NonNull List<Article> articles) {

    }

    @Override
    public void refreshArticles() {

    }

    @Override
    public void deleteAllArticles() {

    }

    @Override
    public void deleteArticle(@NonNull String ArticleId) {

    }

}

package com.example.xyzreader.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.source.ArticlesDataSource;
import com.example.xyzreader.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nestor on 2/28/18.
 */

public class ArticleLocalDataSoure implements ArticlesDataSource {

    private static ArticleLocalDataSoure mArticleLocalDataSoure;
    int _ID = 0;
    int TITLE = 1;
    int PUBLISHED_DATE = 2;
    int AUTHOR = 3;
    int THUMB_URL = 4;
    int PHOTO_URL = 5;
    int ASPECT_RATIO = 6;
    int BODY = 7;
    private ContentResolver mContentResolver;
    private AppExecutors mAppExecutors;

    private ArticleLocalDataSoure(ContentResolver contentResolver, AppExecutors appExecutors) {
        mContentResolver = contentResolver;
        mAppExecutors = appExecutors;
    }

    public static ArticleLocalDataSoure getNewInstance(@NonNull ContentResolver contentResolver,
                                                       @NonNull AppExecutors appExecutors) {

        if (mArticleLocalDataSoure != null) return mArticleLocalDataSoure;

        return new ArticleLocalDataSoure(contentResolver, appExecutors);
    }

    @Override
    public void getArticles(@NonNull final LoadArticlesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mContentResolver.query(ItemsContract.Items.buildDirUri(),
                        Query.PROJECTION,
                        null,
                        null,
                        null);

                cursor.moveToFirst();
                final List<Article> articleList = new ArrayList<>();

                while (cursor.moveToNext()) {
                    Article article = articleValueOf(cursor);
                    articleList.add(article);
                }

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (articleList.isEmpty()) callback.onDataNotAvailable();
                        else callback.onArticlesLoaded(articleList);
                    }
                });

            }
        };
        mAppExecutors.diskIO().execute(runnable);

    }

    @Override
    public void getArticle(@NonNull final String ArticleId, @NonNull final GetArticleCallback callback) {
        Runnable runnable = new Runnable() {
            Article article = null;

            @Override
            public void run() {
                final String[] args = {ArticleId};
                Cursor cursor = mContentResolver.query(ItemsContract.Items.buildDirUri(),
                        Query.PROJECTION,
                        ItemsContract.Items._ID + "=?",
                        args,
                        null);
                Log.d("OUT GetArticle Article" + cursor.getCount(), article.toString());
                if (cursor != null) {
                    cursor.moveToFirst();
                    article = articleValueOf(cursor);
                    Log.d("IN GetArticle Article" + cursor.getCount(), article.toString());
                }

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (article != null) callback.onArticleLoaded(article);
                        else callback.onDataNotAvailable();
                    }
                });
            }
        };


    }

    @Override
    public void getArticleWithFullContent(@NonNull String ArticleId, @NonNull GetArticleCallback callback) {
        getArticle(ArticleId, callback);
    }

    private Article articleValueOf(@NonNull Cursor cursor) {
        Article article = new Article();
        article.set_ID(cursor.getString(_ID));
        article.setBODY(cursor.getString(BODY));
        article.setTITLE(cursor.getString(TITLE));
        article.setAUTHOR(cursor.getString(AUTHOR));
        article.setTHUMB_URL(cursor.getString(THUMB_URL));
        article.setPHOTO_URL(cursor.getString(PHOTO_URL));
        article.setASPECT_RATIO(cursor.getString(ASPECT_RATIO));
        article.setPUBLISHED_DATE(cursor.getString(PUBLISHED_DATE));

        return article;
    }

    @Override
    public void saveArticle(@NonNull Article article) {
        final Article article1 = article;
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mContentResolver.insert(ItemsContract.Items.buildDirUri(), article1.getContentValue());
            }
        });

    }

    @Override
    public void saveArticle(@NonNull List<Article> articles) {

        final List<Article> articleList = articles;
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final ContentValues[] valuesArrayList = new ContentValues[articleList.size()];
                int count = 0;
                for (Article article : articleList) {
                    valuesArrayList[count] = article.getContentValue();
                    count++;
                }
                mContentResolver.bulkInsert(ItemsContract.Items.buildDirUri(), valuesArrayList);
            }
        });

    }

    @Override
    public void refreshArticles() {

    }

    @Override
    public void deleteAllArticles() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mContentResolver.delete(ItemsContract.Items.buildDirUri(), null, null);

            }
        });


    }

    @Override
    public void deleteArticle(@NonNull String ArticleId) {

    }
    public interface Query {
        String[] PROJECTION = {
                ItemsContract.Items._ID,
                ItemsContract.Items.TITLE,
                ItemsContract.Items.PUBLISHED_DATE,
                ItemsContract.Items.AUTHOR,
                ItemsContract.Items.THUMB_URL,
                ItemsContract.Items.PHOTO_URL,
                ItemsContract.Items.ASPECT_RATIO,
                ItemsContract.Items.BODY,
        };
    }

}
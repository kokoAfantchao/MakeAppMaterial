/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.xyzreader.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.source.local.ArticleLocalDataSoure;
import com.example.xyzreader.data.source.remote.ArticleRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * Concrete implementation to load Articles from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class ArticlesRepository implements ArticlesDataSource {

    private static ArticlesRepository INSTANCE = null;

    private final ArticlesDataSource mArticlesRemoteDataSource;

    private final ArticlesDataSource mArticlesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
     Map<String, Article> mCachedArticles;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ArticlesRepository(@NonNull ArticlesDataSource ArticlesRemoteDataSource,
                               @NonNull ArticlesDataSource ArticlesLocalDataSource) {
        mArticlesRemoteDataSource = checkNotNull(ArticlesRemoteDataSource);
        mArticlesLocalDataSource = checkNotNull(ArticlesLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param ArticlesRemoteDataSource the backend data source
     * @param ArticlesLocalDataSource  the device storage data source
     * @return the {@link ArticlesRepository} instance
     */
    public static ArticlesRepository getInstance(ArticleRemoteDataSource ArticlesRemoteDataSource,
                                              ArticleLocalDataSoure ArticlesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ArticlesRepository(ArticlesRemoteDataSource, ArticlesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ArticlesDataSource, ArticlesDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets Articles from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadArticlesCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getArticles(@NonNull final LoadArticlesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedArticles != null && !mCacheIsDirty) {
            callback.onArticlesLoaded(new ArrayList<>(mCachedArticles.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getArticlesFromRemoteDataSource(callback);
        } else{
            // Query the local storage if available. If not, query the network.
           mArticlesLocalDataSource.getArticles(callback);
//            mArticlesLocalDataSource.getArticles(new LoadArticlesCallback() {
//                @Override
//                public void onArticlesLoaded(List<Article> Articles) {
//                    refreshCache(Articles);
//                    callback.onArticlesLoaded(new ArrayList<>(mCachedArticles.values()));
//                }
//
//                @Override
//                public void onDataNotAvailable() {
//                    getArticlesFromRemoteDataSource(callback);
//                }
//            });
        }
    }

    @Override
    public void saveArticle(@NonNull Article Article) {
        checkNotNull(Article);
        mArticlesRemoteDataSource.saveArticle(Article);
        mArticlesLocalDataSource.saveArticle(Article);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedArticles == null) {
            mCachedArticles = new LinkedHashMap<>();
        }
        mCachedArticles.put(Article.get_ID(), Article);
    }

    @Override
    public void saveArticle(@NonNull List<Article> articles) {

    }


    /**
     * Gets Articles from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetArticleCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getArticle(@NonNull final String ArticleId, @NonNull final GetArticleCallback callback) {
        checkNotNull(ArticleId);
        checkNotNull(callback);

        Article cachedArticle = getArticleWithId(ArticleId);

        // Respond immediately with cache if available
        if (cachedArticle != null) {
            callback.onArticleLoaded(cachedArticle);
            return;
        }

        // Load from server/persisted if needed.

        // Is the Article in the local data source? If not, query the network.
        mArticlesLocalDataSource.getArticle(ArticleId, new GetArticleCallback() {
            @Override
            public void onArticleLoaded(Article Article) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedArticles == null) {
                    mCachedArticles = new LinkedHashMap<>();
                }
                mCachedArticles.put(Article.get_ID(), Article);
                callback.onArticleLoaded(Article);
            }

            @Override
            public void onDataNotAvailable() {
                mArticlesRemoteDataSource.getArticle(ArticleId, new GetArticleCallback() {
                    @Override
                    public void onArticleLoaded(Article Article) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedArticles == null) {
                            mCachedArticles = new LinkedHashMap<>();
                        }
                        mCachedArticles.put(Article.get_ID(), Article);
                        callback.onArticleLoaded(Article);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshArticles() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllArticles() {
        mArticlesRemoteDataSource.deleteAllArticles();
        mArticlesLocalDataSource.deleteAllArticles();

        if (mCachedArticles == null) {
            mCachedArticles = new LinkedHashMap<>();
        }
        mCachedArticles.clear();
    }

    @Override
    public void deleteArticle(@NonNull String ArticleId) {
        mArticlesRemoteDataSource.deleteArticle(checkNotNull(ArticleId));
        mArticlesLocalDataSource.deleteArticle(checkNotNull(ArticleId));

        mCachedArticles.remove(ArticleId);
    }

    private void getArticlesFromRemoteDataSource(@NonNull final LoadArticlesCallback callback) {
        mArticlesRemoteDataSource.getArticles(new LoadArticlesCallback() {
            @Override
            public void onArticlesLoaded(List<Article> Articles) {
                refreshCache(Articles);
                refreshLocalDataSource(Articles);
                callback.onArticlesLoaded(new ArrayList<>(mCachedArticles.values()));
            }

            @Override
            public void onDataNotAvailable(){
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Article> Articles) {
        if (mCachedArticles == null) {
            mCachedArticles = new LinkedHashMap<>();
        }
        mCachedArticles.clear();
        for (Article article : Articles) {
            article.reduceBodyLength();
            mCachedArticles.put(article.get_ID(), article);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Article> Articles) {
        mArticlesLocalDataSource.deleteAllArticles();
        mArticlesLocalDataSource.saveArticle(Articles);

    }

    @Nullable
    private Article getArticleWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedArticles == null || mCachedArticles.isEmpty()) {
            return null;
        } else {
            return mCachedArticles.get(id);
        }
    }
}

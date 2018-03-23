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


import com.example.xyzreader.data.Article;

import java.util.List;

/**
 * Main entry point for accessing Articles data.
 * <p>
 * For simplicity, only getArticles() and getArticle() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new Article is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface ArticlesDataSource {

    interface LoadArticlesCallback {

        void onArticlesLoaded(List<Article> Articles);

        void onDataNotAvailable();
    }

    interface GetArticleCallback {

        void onArticleLoaded(Article Article);

        void onDataNotAvailable();
    }

    void getArticles(@NonNull LoadArticlesCallback callback);

    void getArticle(@NonNull String ArticleId, @NonNull GetArticleCallback callback);

    void saveArticle(@NonNull Article Article);
    void saveArticle(@NonNull List<Article> articles );

    void refreshArticles();

    void deleteAllArticles();

    void deleteArticle(@NonNull String ArticleId);
}

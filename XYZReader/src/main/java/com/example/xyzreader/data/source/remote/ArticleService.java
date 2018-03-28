package com.example.xyzreader.data.source.remote;

import com.example.xyzreader.data.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by nestor on 2/27/18.
 */

public interface ArticleService {

    @GET("xyz-reader-json")
    Call<List<Article>> listRepos();

}

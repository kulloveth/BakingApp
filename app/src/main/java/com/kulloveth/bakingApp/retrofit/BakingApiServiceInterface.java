package com.kulloveth.bakingApp.retrofit;

import com.kulloveth.bakingApp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApiServiceInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipe();
}

package com.kulloveth.bakingApp.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kulloveth.bakingApp.ApiUtils;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.retrofit.BakingApiServiceInterface;
import com.kulloveth.bakingApp.utils.ProgressListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private BakingApiServiceInterface apiServiceInterface;
    private MutableLiveData<List<Recipe>> recipeLivedata;
    private MutableLiveData<Recipe> recipeMutableLiveData;
    private ProgressListener progressListener;

    public MainActivityViewModel() {
        apiServiceInterface = ApiUtils.getBakingApiServiceInterface();
        recipeLivedata = new MutableLiveData<>();
        recipeMutableLiveData = new MutableLiveData<>();
    }

    public void setRecipeLivedata(Recipe recipe) {
        recipeMutableLiveData.setValue(recipe);
    }

    public LiveData<Recipe> getRecipeLivedata() {
        return recipeMutableLiveData;
    }

    public LiveData<List<Recipe>> getRecipe() {
        progressListener.showLoading();
        apiServiceInterface.getRecipe().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    progressListener.showRecipes();
                    recipeLivedata.setValue(response.body());
                } else {
                    Log.d(TAG, "onResponse: could not fetch recipe");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressListener.showNoInternet();
                Log.e(TAG, "onFailure: Error has occured" + t.getMessage());
            }
        });
        return recipeLivedata;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }
}

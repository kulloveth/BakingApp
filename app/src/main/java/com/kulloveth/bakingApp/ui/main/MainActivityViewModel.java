package com.kulloveth.bakingApp.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kulloveth.bakingApp.ApiUtils;
import com.kulloveth.bakingApp.model.Recipe;
import com.kulloveth.bakingApp.retrofit.BakingApiServiceInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private BakingApiServiceInterface apiServiceInterface;
    private MutableLiveData<List<Recipe>> recipeLivedata;

    public MainActivityViewModel() {
        apiServiceInterface = ApiUtils.getBakingApiServiceInterface();
        recipeLivedata = new MutableLiveData<>();
    }


    public LiveData<List<Recipe>> getRecipe() {
        apiServiceInterface.getRecipe().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipeLivedata.setValue(response.body());
                } else {
                    Log.d(TAG, "onResponse: could not fetch recipe");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error has occured" + t.getMessage());
            }
        });
        return recipeLivedata;
    }


}

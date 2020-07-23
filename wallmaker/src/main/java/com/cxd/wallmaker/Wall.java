package com.cxd.wallmaker;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Wall<Bean> {
    private Call call ;

    public Wall(Call call) {
        this.call = call;
    }

    public void request(IAccept<Bean> iAccept){
        request(iAccept, null);
    }

    @SuppressLint("CheckResult")
    public void request(final IAccept<Bean> iAccept, final IFAccept ifAccept){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null ;
        request = new Request.Builder()
                .url("http://47.103.217.160:8080/bodybuilding/version/get")
                .get()
                .build();
        Call call1 = okHttpClient.newCall(request);

        call1.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if(ifAccept != null){
                    ifAccept.onError(e);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response != null && response.body() != null){
                    String s = response.body().string();
                    Log.i("WallMaker", "onResponse: "+s);
//                    Gson gson = new Gson();
//                    iAccept.onSuccess(gson.fromJson(s,T);
                }else{
                    iAccept.onSuccess(null);
                }
            }
        });
    }

    public interface IAccept<Bean>{
        void onSuccess(Bean t);
    }
    public interface IFAccept{
        void onError(IOException e);
    }
}

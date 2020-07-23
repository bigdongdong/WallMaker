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


/**
 * Bean 是正向的逻辑（传过来的，但IDE会自动补全）
 * type 是反向的逻辑（Service中定义的方法的返回值泛型，是从Bean获得的）
 * @param <Bean>
 */
public final class Wall<Bean> {
    private Call call ;
    private Type type ;

    public Wall(Call call , Type type) {
        this.call = call;
        this.type = type;
    }

    public void request(IAccept<Bean> iAccept){
        request(iAccept, null);
    }

    @SuppressLint("CheckResult")
    public void request(final IAccept<Bean> iAccept, final IFAccept ifAccept){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if(ifAccept != null){
                    ifAccept.onError(e);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response != null && response.body() != null){
                    String s = shell(response.body().string()); //脱壳
                    iAccept.onSuccess((Bean)new Gson().fromJson(s,type));
                }else if(ifAccept != null){
                    ifAccept.onError(new IOException("response is null"));
                }
            }
        });
    }

    /**
     * 脱壳
     * @param parent 脱壳前的父泛型
     * @return 脱壳后的子泛型
     */
    private String shell(String parent){
        return parent;
    }

    public interface IAccept<Bean>{
        void onSuccess(Bean bean);
    }
    public interface IFAccept{
        void onError(IOException e);
    }
}

package com.cxd.wallmaker;

import android.annotation.SuppressLint;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
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
                    Observable.just(e)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<IOException>() {
                                @Override
                                public void accept(IOException e) throws Exception {
                                    ifAccept.onError(e);
                                }
                            });
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response != null && response.body() != null){
                    final String s = shell(response.body().string()); //脱壳
                    Observable.just(s)
                            .map(new Function<String, Bean>() {
                                @Override
                                public Bean apply(String s) throws Exception {
                                    return new Gson().fromJson(s,type);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Bean>() {
                                @Override
                                public void accept(Bean b) throws Exception {
                                    iAccept.onSuccess(b);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if(ifAccept != null){
                                        ifAccept.onError(new Exception(throwable.getMessage() + "\n"+ s));
                                    }
                                }
                            });
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
        void onError(Exception e);
    }
}

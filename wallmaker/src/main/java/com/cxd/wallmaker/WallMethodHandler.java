package com.cxd.wallmaker;

import android.util.Log;

import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.HEADER;
import com.cxd.wallmaker.http.method.POST;
import com.cxd.wallmaker.http.param.Param;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * create by chenxiaodong on 2020/7/23
 * service's method 动态代理
 */
public class WallMethodHandler implements InvocationHandler {
    private static final String TAG = "WallMaker";
    private final String baseUrl ;

    public WallMethodHandler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    //通过动态代理
    public Object newProxyInstance(Class c) {
        try{
            Class calculatorProxyClazz = Proxy.getProxyClass(c.getClassLoader(),c);
            //得到有参构造器
            Constructor constructor = calculatorProxyClazz.getConstructor(InvocationHandler.class);
            //反射创建代理实例
            return constructor.newInstance(this);
        }catch (Exception e){}

        return null ;
    }

    //Object proxy:被代理的对象，也就是service
    //Method method:要调用的方法
    //Object[] args:方法调用时所需要参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /*先获取方法的注解*/
        Annotation[] annos = method.getDeclaredAnnotations();
        /*请求参数*/
        String url = null ;
        RequestMethod httpMethod = null ;  //请求方法
        String[] headers = null;
        Map<String,Object> params = null;

        for(Annotation a : annos){
            if(a instanceof GET){
                httpMethod = RequestMethod.GET ;
                url = ((GET)a).value();
            }else if(a instanceof POST){
                httpMethod = RequestMethod.POST ;
                url = ((POST)a).value();
            }else if(a instanceof HEADER){
                headers = ((HEADER)a).value() ;
            }
        }

        /*解析参数的注解*/
        Annotation[][] pannos = method.getParameterAnnotations();
        Map<String,Object> map ;
        if(args != null && args.length > 0){
            for (int i = 0; i < args.length; i++) {
                Annotation a = pannos[i][0];//只取每个参数的第一个注解
                if(a instanceof Param){
                    if(params == null){
                        params = new HashMap<>();
                    }
                    map = new HashMap<>();
                    map.put(((Param)a).value(),args[i]);
                }
            }
        }

        /*构造OkHttp3请求*/
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null ;
        if(httpMethod == RequestMethod.GET){
            /*封装参数*/
            String param = ""; //参数后缀
            if(params != null && params.size() > 0){
                StringBuilder sb = new StringBuilder("?");
                Set<Map.Entry<String,Object>> entries = params.entrySet();
                for(Map.Entry<String,Object> entry : entries){
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append("&");
                }
                param = sb.toString();
                param = param.substring(0,param.length()-2);
            }
            request = new Request.Builder()
                    .url(baseUrl + url + param)
                    .method(httpMethod.value,null)
                    .build();
        }else if(httpMethod == RequestMethod.POST){
            final MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody formBody = RequestBody.create(params.toString().getBytes(),JSON);
            request = new Request.Builder()
                    .url(baseUrl + url)
                    .method(httpMethod.value,formBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        Class<? extends Object> returnTypeClass = method.getReturnType();
        Log.i(TAG, "invoke: " + returnTypeClass);
        if(returnTypeClass == Wall.class){
            /*拿到对应的泛型T*/
            Type type = Wall.class.getGenericSuperclass();
            Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
            Log.i(TAG, "invoke: "+type);
            Log.i(TAG, "invoke: "+trueType);
        }
        Log.i(TAG, "invoke: "+method.getReturnType());
//        Type type = returnType.getClass().getGenericSuperclass();
//        Log.i(TAG, "invoke: "+type);
//        type = ((ParameterizedType) type).getActualTypeArguments()[0]; //获取第一个泛型
////        if(returnType.getClass() == Wall.class){
////
////
////        }
        Wall wall = new Wall<>(call);
        return wall;
    }
}

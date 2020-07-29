package com.cxd.wallmaker;

import android.util.Log;

import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.HEADERS;
import com.cxd.wallmaker.http.method.POST;
import com.cxd.wallmaker.http.param.Param;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * create by chenxiaodong on 2020/7/23
 * service's method 动态代理
 */
public class WallMethodHandler implements InvocationHandler {
    private final String TAG = "WallMaker";
    private final String baseUrl ;
    private final Headers baseHeaders ;

    public WallMethodHandler(String baseUrl , Headers baseHeaders) {
        this.baseUrl = baseUrl;
        this.baseHeaders = baseHeaders;
    }

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

    @Override
    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) throws Throwable {
        /*先获取方法的注解*/
        Annotation[] annos = method.getDeclaredAnnotations();
        /*请求地址*/
        String url = null ;
        /*请求方法*/
        Method httpMethod = null ;
        /*请求参数集合*/
        Map<String,String> paramsMap = new HashMap<>();
        /*默认公用请求头*/
        Headers headers = (baseHeaders != null) ? baseHeaders
                : new Headers.Builder().add("Content-Type","application/json").build();

        /*解析所有的annos*/
        for(Annotation a : annos){
            if(a instanceof GET){
                httpMethod = Method.GET ;
                url = ((GET)a).url();
            }else if(a instanceof POST){
                httpMethod = Method.POST ;
                url = ((POST)a).url();
            }
            else if(a instanceof HEADERS){
                String[] s = ((HEADERS)a).headers() ;
                if(s != null){
                    Headers.Builder builder = new Headers.Builder();
                    for (int i = 0; i < s.length; i++) {
                        builder.add(s[i]);
                    }
                    headers = builder.build();
                }
            }
        }

        /*解析参数*/
        Annotation[][] pannos = method.getParameterAnnotations();
        if(args != null && args.length > 0){
            for (int i = 0; i < args.length; i++) {
                Annotation a = pannos[i][0];//只取每个参数的第一个注解
                if(a instanceof Param){
                    paramsMap.put(((Param)a).key(),args[i].toString());
                }
            }
        }

        /*构造OkHttp3请求*/
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null ;
        if(httpMethod == Method.GET){ /*GET*/
            /*封装GET参数*/
            String param = ""; //参数后缀
            if(paramsMap != null && paramsMap.size() > 0){
                StringBuilder sb = new StringBuilder("?");
                Set<Map.Entry<String,String>> entries = paramsMap.entrySet();
                for(Map.Entry<String,String> entry : entries){
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append("&");
                }
                param = sb.toString();
                param = param.substring(0,param.length()-1);
            }
            /*实例化GET请求的request*/
            request = new Request.Builder()
                    .url(baseUrl + url + param)
                    .headers(headers)
                    .method(httpMethod.value,null)
                    .build();
        }else if(httpMethod == Method.POST){ /*POST*/
            /*封装POST请求的参数*/
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if(paramsMap != null && paramsMap.size() > 0){
                Set<Map.Entry<String,String>> entries = paramsMap.entrySet();
                for(Map.Entry<String,String> entry : entries){
                    formBodyBuilder.add(entry.getKey(),entry.getValue());
                }
            }
            /*实例化POST请求的request*/
            request = new Request.Builder()
                    .url(baseUrl + url)
                    .headers(headers)
                    .method(httpMethod.value,formBodyBuilder.build())
                    .build();
        }

        /*通过request构造Call*/
        Call call = okHttpClient.newCall(request);
        Type type = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        Wall wall = new Wall<>(call,type);
        return wall;
    }
}

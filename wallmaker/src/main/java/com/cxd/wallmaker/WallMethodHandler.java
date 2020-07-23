package com.cxd.wallmaker;

import android.util.Log;

import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.POST;
import com.cxd.wallmaker.http.param.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
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

    public WallMethodHandler(String baseUrl) {
        this.baseUrl = baseUrl;
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
        /*请求参数*/
        String url = null ; //请求地址
        Method httpMethod = null ;  //请求方法
        Map<String,Object> paramsMap = null; //请求参数集合
//        String[] headers = null;

        for(Annotation a : annos){
            if(a instanceof GET){
                httpMethod = Method.GET ;
                url = ((GET)a).url();
            }else if(a instanceof POST){
                httpMethod = Method.POST ;
                url = ((POST)a).url();
            }
//            else if(a instanceof HEADER){
//                headers = ((HEADER)a).value() ;
//            }
        }

        /*解析参数的注解*/
        Annotation[][] pannos = method.getParameterAnnotations();
        if(args != null && args.length > 0){
            for (int i = 0; i < args.length; i++) {
                Annotation a = pannos[i][0];//只取每个参数的第一个注解
                if(a instanceof Param){
                    if(paramsMap == null){
                        paramsMap = new HashMap<>();
                    }
                    paramsMap.put(((Param)a).key(),args[i]);
                }
            }
        }

        /*构造OkHttp3请求*/
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null ;
        if(httpMethod == Method.GET){ //GET
            /*封装GET参数*/
            String param = ""; //参数后缀
            if(paramsMap != null && paramsMap.size() > 0){
                StringBuilder sb = new StringBuilder("?");
                Set<Map.Entry<String,Object>> entries = paramsMap.entrySet();
                for(Map.Entry<String,Object> entry : entries){
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
                    .method(httpMethod.value,null)
                    .build();
        }else if(httpMethod == Method.POST){ //POST
            /*封装POST请求的参数*/
            final MediaType JSON = MediaType.get("application/json; charset=utf-8"); //默认json格式的MediaType
            RequestBody formBody = RequestBody.create(paramsMap.toString().getBytes(),JSON);
            /*实例化POST请求的request*/
            request = new Request.Builder()
                    .url(baseUrl + url)
                    .method(httpMethod.value,formBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        Type type = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        Wall wall = new Wall<>(call,type);
        return wall;
    }
}

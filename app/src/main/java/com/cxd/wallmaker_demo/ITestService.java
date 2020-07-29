package com.cxd.wallmaker_demo;


import com.cxd.wallmaker.Wall;
import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.HEADERS;
import com.cxd.wallmaker.http.method.POST;
import com.cxd.wallmaker.http.param.Param;

public interface ITestService {

    @GET(url = "/test/get")
    Wall<Integer> get();

    @GET(url = "/test/getp")
    Wall<Integer> get(@Param(key = "param")String param);

    @POST(url = "/test/post")
    Wall<Integer> post();

    @HEADERS(headers = {"Date:111111","user:cxd"})
    @POST(url = "/test/postp")
    Wall<Integer> post(@Param(key = "param")String param);
}

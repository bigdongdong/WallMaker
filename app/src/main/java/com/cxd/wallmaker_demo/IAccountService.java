package com.cxd.wallmaker_demo;


import com.cxd.wallmaker.Wall;
import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.POST;
import com.cxd.wallmaker.http.param.Param;

public interface IAccountService {

    @GET(url = "/version/get")
    Wall<VersionBean> get(@Param(key = "first")String first);

    @GET(url = "/account/login")
    Wall<Object> login(@Param(key = "name")String name , @Param(key = "password")String password);
}

package com.cxd.wallmaker_demo;


import com.cxd.wallmaker.Wall;
import com.cxd.wallmaker.http.method.GET;
import com.cxd.wallmaker.http.method.POST;

public interface IAccountService {

    @GET(value = "/version/get")
    Wall<VersionBean> get();
}

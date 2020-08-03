package com.cxd.wallmaker_demo;

import com.cxd.wallmaker.WallMaker;

public class WallMakerService {
//    private static final String baseUrl = "http://47.103.217.160:8080/bodybuilding" ;
    private static final String baseUrl = "http://192.168.71.222:8080/bodybuilding" ;

    /**
     * 通过传入Service.class创建一个实例
     * @return
     */
    public static <S>S create(Class<S> c){
        return new WallMaker.Builder()
                .baseUrl(baseUrl)
//                .preTreat(new WallMaker.Builder.IPreProcess<Common<>>() {
//                })
                .build()
                .create(c);
    }
}

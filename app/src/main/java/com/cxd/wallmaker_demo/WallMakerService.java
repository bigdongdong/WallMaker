package com.cxd.wallmaker_demo;

import com.cxd.wallmaker.WallMaker;

public class WallMakerService {
//    private static final String baseUrl = "http://47.103.217.160:8080/bodybuilding" ;
    private static final String baseUrl = "http://47.103.217.160:1010/daily" ;

    /**
     * 通过传入Service.class创建一个实例
     * @param Class<S> c : Service.class
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

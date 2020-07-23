package com.cxd.wallmaker_demo;

import com.cxd.wallmaker.WallMaker;

public class WallMakerService {

    public static <T>T create(Class<T> c){
        return new WallMaker.Builder()
                .baseUrl("http://47.103.217.160:8080/bodybuilding")
//                .preProcess(new WallMaker.Builder.IPreProcess() {
//                    @Override
//                    public Object preProcess(Object o) {
//                        return null;
//                    }
//                })
                .build()
                .create(c);
    }
}

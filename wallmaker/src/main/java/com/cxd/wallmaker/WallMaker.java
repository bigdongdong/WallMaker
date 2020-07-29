package com.cxd.wallmaker;

import okhttp3.Headers;

public class WallMaker {
    private Builder builder ;

    public WallMaker(Builder builder) {
        this.builder = builder;
    }

    public synchronized <S>S create(Class<S> c){
        if(c != null && c.isInterface()){
            S s = (S) new WallMethodHandler(builder.baseUrl,builder.baseHeaders).newProxyInstance(c);
            return s;
        }else if(!c.isInterface()){
            throw new RuntimeException(c.getName() +" is not an Interface");
        }else{
            return null;
        }
    }

    public static class Builder{
        private String baseUrl ;
        private Headers baseHeaders ;

        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl ;
            return this ;
        }

        public Builder baseHeaders(Headers baseHeaders){
            this.baseHeaders = baseHeaders ;
            return this ;
        }

        public WallMaker build(){
            return new WallMaker(this);
        }
    }


}

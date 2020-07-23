package com.cxd.wallmaker;

public class WallMaker {
    private Builder builder ;

    public WallMaker(Builder builder) {
        this.builder = builder;
    }

    public <S>S create(Class<S> c){
        if(c != null && c.isInterface()){
            S s = (S) new WallMethodHandler(builder.baseUrl).newProxyInstance(c);
            return s;
        }else if(!c.isInterface()){
            throw new RuntimeException(c.getName() +" is not an Interface");
        }else{
            return null;
        }
    }

    public static class Builder{
//        private String baseHeader ;
        private IPreProcess iPreProcess ;
        private String baseUrl ;

//        public Builder baseHeader(String baseHeader){
//            this.baseHeader = baseHeader ;
//            return this;
//        }

        public Builder preProcess(IPreProcess i){
            this.iPreProcess = i ;
            return this ;
        }

        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl ;
            return this ;
        }

        public WallMaker build(){
            return new WallMaker(this);
        }

        /**
         * @param <T>
         */
        public interface IPreProcess<M,T>{
                T preProcess(M m); //从M里剖析出T
        }
    }


}

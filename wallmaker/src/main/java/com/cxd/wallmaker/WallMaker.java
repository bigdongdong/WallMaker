package com.cxd.wallmaker;

public class WallMaker {
    private Builder builder ;

    public WallMaker(Builder builder) {
        this.builder = builder;
    }

    public synchronized <S>S create(Class<S> c){
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
//        private IPreProcess iPreProcess ;
        private String baseUrl ;

        public Builder baseUrl(String baseUrl){
            this.baseUrl = baseUrl ;
            return this ;
        }

        public WallMaker build(){
            return new WallMaker(this);
        }

//        /**
//         * 脱壳的实现回调
//         * @param <Parent> parent 父泛型
//         * @param <Child> child 子泛型
//         */
//        public interface IPreProcess<Parent,Child>{
//            Child preProcess(Parent p); //从M里剖析出T
//        }
    }


}

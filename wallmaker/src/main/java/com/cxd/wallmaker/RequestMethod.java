package com.cxd.wallmaker;

public enum RequestMethod {
    GET("GET"),POST("POST");
    public String value;

    RequestMethod(String value) {
        this.value = value;
    }
}

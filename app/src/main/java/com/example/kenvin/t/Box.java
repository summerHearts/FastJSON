package com.example.kenvin.t;

/**
 * Created by Kenvin on 2017/11/13.
 */

public class Box <T> {
    private  T data;

    public T getData(){
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

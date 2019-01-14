package com.maoyongxin.myapplication.ui;

/**
 * Created by yusr on 2018/8/22.
 */

public class EventMessage<T> {
    private int type;
    private T data;

    public EventMessage(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}


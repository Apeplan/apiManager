package com.xiaomingming.api.utils;

/**
 * Created by Administrator on 2017/10/20.
 */
public class ProjectResult {

    public int status;
    public String msg;
    public Object data;

    public ProjectResult(Object data) {
        this.data = data;
        this.status = 0;
        this.msg = "success";
    }

    public ProjectResult(String err_msg, int status) {
        this.data = new Object();
        this.status = status;
        this.msg = err_msg;
    }
}

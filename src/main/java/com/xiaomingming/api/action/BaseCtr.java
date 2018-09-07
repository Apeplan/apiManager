package com.xiaomingming.api.action;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.xiaomingming.api.utils.ProjectResult;
import com.xiaomingming.api.vo.UsUser;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/29.
 * 所有返回json数据的基类
 */
public abstract class BaseCtr extends Controller {

    protected Map<String, Object> resultMap = new HashMap<String, Object>();
    protected List<Object> resultList = new ArrayList<>();

    protected Logger logger = Logger.getLogger(this.getClass());

    protected UsUser mCurrentUser = null;

    protected boolean is_login() {
        UsUser user = UsUser.dao.findById(getPara("us_id"));
        if (user == null) {
            needLogin();
            return false;
        } else {
            mCurrentUser = user;
            return true;
        }
    }

    private void needLogin() {
        onErr("请重新登陆后再试试");
    }

    public void onOk(Object data) {
        HttpServletResponse response = getResponse();
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        renderJson(JSON.toJSONString(new ProjectResult(data)));
    }

    public void onErr(String msg) {
        onErr(msg, -1);
    }

    public void onErr(String msg, int code) {
        HttpServletResponse response = getResponse();
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        renderJson(JSON.toJSONString(new ProjectResult(msg, code)));
    }

}

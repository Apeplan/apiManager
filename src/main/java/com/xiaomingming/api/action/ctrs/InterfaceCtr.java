package com.xiaomingming.api.action.ctrs;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.action.ProjectValidator;
import com.xiaomingming.api.service.InterfaceService;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.InInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public class InterfaceCtr extends BaseCtr {

    private InterfaceService service = new InterfaceService();

    public void getInterfaces() {
        String fo_id = getPara("fo_id");
        if (StrKit.isBlank(fo_id)) {
            onErr("参数有误,请重试");
            return;
        }
        try {
            List<InInterface> list = service.getAllInterfaces(Integer.parseInt(fo_id));
            for (InInterface item : list) {
                item.setInResponseErr("");
                item.setInResponseOk("");
            }
            onOk(list);
        } catch (Exception e) {
            logger.info(e);
            onErr("接口列表获取失败: " + e.getMessage());
        }
    }

    @Before(ProjectValidator.class)
    public void saveInterface() {
        if (is_login()) {
            String fo_id = getPara("fo_id");
            String in_id = getPara("in_id");
            String in_url = getPara("in_url");
            String in_name = getPara("in_name");
            String params = getPara("params");
            String responsesOk = getPara("in_response_ok");
            String responsesErr = getPara("in_response_err");
            try {
                if ("0".equals(in_id)) {
                    onOk(service.createItem(fo_id, in_url, in_name, params, responsesOk, responsesErr));
                } else {
                    onOk(service.updateItem(fo_id, in_id, in_url, in_name, params, responsesOk, responsesErr));
                }
            } catch (Exception e) {
                logger.info(e);
                onErr("接口列表获取失败: " + e.getMessage());
            }
        }
    }

    public void getDetail() {
        String in_id = getPara("in_id");
        if (StrKit.isBlank(in_id)) {
            onErr("参数有误,请重试");
            return;
        }
        try {
            onOk(service.getInterfaceByID(Integer.parseInt(in_id)));
        } catch (Exception e) {
            logger.info(e);
            onErr("接口详情获取失败: " + e.getMessage());
        }
    }

    public void deleteDoc() {
        if (is_login()) {
            String in_id = getPara("in_id");
            if (StrKit.isBlank(in_id)) {
                onErr("接口有误,请重试");
                return;
            }
            try {
                onOk(service.deleteDocByID(Integer.parseInt(in_id)));
            } catch (Exception e) {
                logger.info(e);
                onErr("删除失败: " + e.getMessage());
            }
        }
    }

    public void getParams() {
        String in_id = getPara("in_id");
        if (StrKit.isBlank(in_id)) {
            onErr("参数有误,请重试");
            return;
        }
        try {
            onOk(service.getParamsByInID(Integer.parseInt(in_id)));
        } catch (Exception e) {
            logger.info(e);
            onErr("接口请求参数获取失败: " + e.getMessage());
        }
    }

    public void getSendResponse() {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String requestUrl = "";
        HashMap<String, String> map = new HashMap<String, String>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            String val = request.getParameter(key);
            if (key.equals("requestUrl")) {
                requestUrl = val;
            } else {
                map.put(key, val);
            }
        }
        try {
            if (!StrKit.isBlank(requestUrl)) {
                renderJson(ProjectUtil.getResultByPOST(requestUrl, map));
            } else {
                onErr("接口requestUrl不能为空");
            }
        } catch (Exception e) {
            onErr("接口请求失败:" + e.getMessage());
        }
    }
}

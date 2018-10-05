package com.xiaomingming.api.action.ctrs;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.action.ProjectValidator;
import com.xiaomingming.api.service.InterfaceService;
import com.xiaomingming.api.utils.HttpClientUtils;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.InInterface;
import com.xiaomingming.api.vo.PaParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
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
            String docStr = getPara("in_doc");
            try {
                if ("0".equals(in_id)) {
                    onOk(service.createItem(fo_id, in_url, in_name, params, responsesOk, responsesErr, docStr));
                } else {
                    onOk(service.updateItem(fo_id, in_id, in_url, in_name, params, responsesOk, responsesErr, docStr));
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
            InInterface data = service.getInterfaceByID(Integer.parseInt(in_id));
            if (data == null) {
                onErr("此接口不存在");
                return;
            }
            resultMap.put("fo_id", data.getFoId());
            resultMap.put("in_id", data.getId());
            resultMap.put("in_url", data.getInUrl());
            resultMap.put("in_name", data.getInName());
            resultMap.put("in_doc", data.getInDocument());
            resultMap.put("in_response_ok", data.getInResponseOk());
            resultMap.put("in_response_err", data.getInResponseErr());
            List<Object> list = new ArrayList<>();
            for (PaParam para : service.getParamsByInID(Integer.parseInt(in_id))) {
                HashMap<String, Object> itemMap = new HashMap<>();
                itemMap.put("id", para.getId());
                itemMap.put("in_id", para.getInId());
                itemMap.put("pa_key", para.getPaKey());
                itemMap.put("pa_value", para.getPaValue());
                itemMap.put("pa_example", para.getPaExample());
                list.add(itemMap);
            }
            resultMap.put("params", list);
            onOk(resultMap);
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
            String key = names.nextElement();
            String val = request.getParameter(key);
            if (key.equals("requestUrl")) {
                requestUrl = val;
            } else {
                map.put(key, val);
            }
        }
        try {
            if (!StrKit.isBlank(requestUrl)) {
                String resStr = HttpClientUtils.post(URI.create(requestUrl), map);
                try {
                    Object obj = JSON.parse(resStr);
                    renderJson(JSON.toJSONString(obj));
                } catch (Exception e) {
                    renderText(resStr);
                }
            } else {
                onErr("接口requestUrl不能为空");
            }
        } catch (Exception e) {
            onErr("接口请求失败:" + e.getMessage());
        }
    }
}

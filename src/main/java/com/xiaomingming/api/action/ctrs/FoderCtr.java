package com.xiaomingming.api.action.ctrs;

import com.jfinal.kit.StrKit;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.service.FoderService;

/**
 * Created by Administrator on 2018/1/2.
 */
public class FoderCtr extends BaseCtr {

    private FoderService service = new FoderService();

    public void getFoders() {
        String pr_id = getPara("pr_id");
        if (StrKit.isBlank(pr_id)) {
            onErr("请重新选择工程");
            return;
        }
        try {
            onOk(service.getAllFoder(Integer.parseInt(pr_id)));
        } catch (Exception e) {
            logger.info(e);
            onErr("文件夹列表获取失败: " + e.getMessage());
        }
    }

    public void addFoder() {
        if (is_login()) {
            String pr_id = getPara("pr_id");
            String foName = getPara("fo_name");
            if (StrKit.isBlank(pr_id)) {
                onErr("请重新选择工程");
                return;
            }
            if (StrKit.isBlank(foName)) {
                onErr("请录入文件夹名");
                return;
            }
            try {
                onOk(service.addFoder(Integer.parseInt(pr_id), foName));
            } catch (Exception e) {
                logger.info(e);
                onErr("文件夹列表获取失败: " + e.getMessage());
            }
        }
    }
}

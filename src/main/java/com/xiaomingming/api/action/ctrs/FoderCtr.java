package com.xiaomingming.api.action.ctrs;

import com.jfinal.kit.StrKit;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.service.FoderService;
import com.xiaomingming.api.service.InterfaceService;
import com.xiaomingming.api.service.ProjectService;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.FoForder;
import com.xiaomingming.api.vo.InInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public class FoderCtr extends BaseCtr {

    private FoderService foderService = new FoderService();
    private ProjectService projectService = new ProjectService();

    public void searchFoders() {
        if (is_login()) {
            String pr_id = getPara("pr_id");
            String search = getPara("search");
            if (ProjectUtil.isEmpty(pr_id)) {
                onErr("请重新选择工程");
                return;
            }
            if (ProjectUtil.isEmpty(search)) {
                onErr("请输入搜索内容");
                return;
            }
            try {
                projectService.checkUser(mCurrentUser.getId(), pr_id);
                onOk(foderService.searchTotal(pr_id, search));
            } catch (Exception e) {
                logger.info(e);
                onErr("文件夹列表获取失败: " + e.getMessage());
            }
        }
    }

    public void getFoders() {
        if (is_login()) {
            String pr_id = getPara("pr_id");
            if (StrKit.isBlank(pr_id)) {
                onErr("请重新选择工程");
                return;
            }
            try {
                projectService.checkUser(mCurrentUser.getId(), pr_id);
                onOk(foderService.queryTotal(pr_id));
            } catch (Exception e) {
                logger.info(e);
                onErr("文件夹列表获取失败: " + e.getMessage());
            }
        }
    }

    public void addFoder() {
        if (is_login()) {
            String pr_id = getPara("pr_id");
            String foName = getPara("fo_name");
            String foId = getPara("fo_id");
            if (StrKit.isBlank(pr_id)) {
                onErr("请重新选择工程");
                return;
            }
            if (StrKit.isBlank(foName)) {
                onErr("请录入文件夹名");
                return;
            }
            try {
                projectService.checkUser(mCurrentUser.getId(), pr_id);
                if (ProjectUtil.isEmpty(foId) || "0".equals(foId)) {
                    onOk(foderService.addFoder(Integer.parseInt(pr_id), foName));
                } else {
                    onOk(foderService.editFoder(Integer.parseInt(foId), foName));
                }
            } catch (Exception e) {
                logger.info(e);
                onErr("文件夹列表获取失败: " + e.getMessage());
            }
        }
    }

    public void delFoder() {
        if (is_login()) {
            String pr_id = getPara("pr_id");
            String foId = getPara("fo_id");
            if (StrKit.isBlank(pr_id)) {
                onErr("请重新选择工程");
                return;
            }
            if (StrKit.isBlank(foId)) {
                onErr("请录入文件夹名");
                return;
            }
            try {
                projectService.checkUser(mCurrentUser.getId(), pr_id);
                onOk(foderService.delFoder(foId));
            } catch (Exception e) {
                logger.info(e);
                onErr("文件夹列表获取失败: " + e.getMessage());
            }
        }
    }
}

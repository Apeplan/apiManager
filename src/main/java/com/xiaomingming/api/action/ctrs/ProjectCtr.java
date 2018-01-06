package com.xiaomingming.api.action.ctrs;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.service.ProjectService;

/**
 * Created by Administrator on 2018/1/2.
 */
public class ProjectCtr extends BaseCtr {

    private ProjectService service = new ProjectService();

    public void getProjects() {
        if (is_login()) {
            try {
                onOk(service.getAllProject(mCurrentUser.getId()));
            } catch (Exception e) {
                logger.info(e);
                onErr("工程列表获取失败: " + e.getMessage());
            }
        }
    }

    @Before(Tx.class)
    public void addProject() {
        if (is_login()) {
            String prName = getPara("pr_name");
            if (StrKit.isBlank(prName)) {
                onErr("请录入工程名");
                return;
            }
            try {
                onOk(service.addProject(mCurrentUser.getId(), prName));
            } catch (Exception e) {
                logger.info(e);
                onErr("添加失败: " + e.getMessage());
            }
        }
    }

}

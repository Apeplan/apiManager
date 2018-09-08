package com.xiaomingming.api.action.ctrs;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.service.FoderService;
import com.xiaomingming.api.service.ProjectService;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.PrProject;
import com.xiaomingming.api.vo.UsUser;
import com.xiaomingming.api.vo.UserProject;

/**
 * Created by Administrator on 2018/1/2.
 */
public class ProjectCtr extends BaseCtr {

    private ProjectService service = new ProjectService();
    private FoderService foderService = new FoderService();

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
    public void editProject() {
        if (is_login()) {
            String prName = getPara("prName");
            String id = getPara("id");
            if (StrKit.isBlank(prName)) {
                onErr("请录入项目名");
                return;
            }
            try {
                if (ProjectUtil.isEmpty(id)) {
                    PrProject project = service.addProject(mCurrentUser.getId(), prName, getPara("prInfo"));
                    onOk(project);
                    foderService.addFoder(project.getId(), "新建文件夹");
                } else {
                    onOk(service.editProject(mCurrentUser.getId(), id, prName, getPara("prInfo")));
                }
            } catch (Exception e) {
                logger.info(e);
                onErr("操作失败: " + e.getMessage());
            }
        }
    }

    @Before(Tx.class)
    public void shardProject() {
        if (is_login()) {
            try {
                String pr_id = getPara("pr_id");
                String to_name = getPara("to_name");
                service.checkUser(mCurrentUser.getId(), pr_id);
                UsUser user = UsUser.dao.queryUserByUserName(to_name);
                if (user == null) {
                    throw new RuntimeException("该用户不存在,请仔细核对");
                } else {
                    UserProject userProject = new UserProject();
                    userProject.setUsId(user.getId());
                    userProject.setPrId(Integer.parseInt(pr_id));
                    onOk(userProject.save());
                }
            } catch (Exception e) {
                onErr(e.getMessage());
            }
        }
    }

    @Before(Tx.class)
    public void delProject() {
        if (is_login()) {
            try {
                String id = getPara("id");
                if (ProjectUtil.isEmpty(id)) {
                    throw new RuntimeException("项目不存在");
                }
                onOk(service.delProject(mCurrentUser.getId(), id));
            } catch (Exception e) {
                logger.info(e);
                onErr("操作失败: " + e.getMessage());
            }
        }
    }

}

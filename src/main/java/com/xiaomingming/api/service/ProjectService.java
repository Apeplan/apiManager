package com.xiaomingming.api.service;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.PrProject;
import com.xiaomingming.api.vo.UserProject;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public class ProjectService {

    private static final PrProject dao = new PrProject().dao();

    private static final String sql_check_user = "select * from user_project where us_id = ? and pr_id = ?";

    public List<PrProject> getAllProject(Integer us_id) {
        return dao.find("SELECT t2.id, t2.pr_name, t2.pr_create_time, t2.pr_info FROM user_project AS t1 INNER JOIN pr_project AS t2 ON t1.pr_id = t2.id WHERE t1.us_id = ?", us_id);
    }

    public void checkUser(Object us_id, String pr_id) {
        PrProject project = PrProject.dao.findById(pr_id);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        UserProject up = UserProject.dao.findFirst(sql_check_user, us_id, pr_id);
        if (up == null) {
            throw new RuntimeException("此项目不属于你");
        }
    }

    @Before(Tx.class)
    public PrProject addProject(Integer us_id, String pr_name, String info) {
        PrProject project = new PrProject();
        project.setPrCreateTime(ProjectUtil.getDBDate());
        project.setPrName(pr_name);
        project.setPrInfo(info);
        if (project.save()) {
            UserProject userProject = new UserProject();
            userProject.setPrId(project.getId());
            userProject.setUsId(us_id);
            userProject.save();
        }
        return project;
    }

    public PrProject editProject(Integer us_id, String pr_id, String prName, String info) {
        checkUser(us_id, pr_id);
        PrProject project = PrProject.dao.findById(pr_id);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        project.setPrCreateTime(ProjectUtil.getDBDate());
        project.setPrName(prName);
        project.setPrInfo(info);
        project.update();
        return project;
    }

    public boolean delProject(Integer us_id, String pr_id) {
        checkUser(us_id, pr_id);
        UserProject up = UserProject.dao.findFirst(sql_check_user, us_id, pr_id);
        up.setUsId(null);
        return up.update();
    }
}

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

    public List<PrProject> getAllProject(Integer us_id) {
        return dao.find("SELECT t2.id, t2.pr_name, t2.pr_create_time, t2.pr_root_url FROM user_project AS t1 INNER JOIN pr_project AS t2 ON t1.pr_id = t2.id WHERE t1.us_id = ?", us_id);
    }

    @Before(Tx.class)
    public PrProject addProject(Integer us_id, String pr_name) {
        PrProject project = new PrProject();
        project.setPrCreateTime(ProjectUtil.getDBDate());
        project.setPrName(pr_name);
        if (project.save()) {
            UserProject userProject = new UserProject();
            userProject.setPrId(project.getId());
            userProject.setUsId(us_id);
            userProject.save();
        }
        return project;
    }
}

package com.xiaomingming.api.service;

import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.FoForder;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public class FoderService {

    private static final FoForder dao = new FoForder().dao();

    public List<FoForder> getAllFoder(Integer pr_id) {
        return dao.find("select * from fo_forder where pr_id = ?", pr_id);
    }

    public FoForder addFoder(int pr_id, String fo_name) {
        FoForder forder = new FoForder();
        forder.setFoCreateTime(ProjectUtil.getDBDate());
        forder.setFoName(fo_name);
        forder.setPrId(pr_id);
        forder.save();
        return forder;
    }
}

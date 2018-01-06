package com.xiaomingming.api.service;

import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.UsUser;

/**
 * Created by Administrator on 2017/12/29.
 */
public class UserService {

    private static final UsUser dao = new UsUser().dao();

    public boolean regist(String phone, String password) {
        UsUser user = dao.findFirst("select * from us_user where us_mobile = ?", phone);
        if (user != null) {
            throw new RuntimeException("用户名已存在");
        }
        user = new UsUser();
        user.setUsName(phone);
        user.setUsPass(password);
        user.setUsMobile(phone);
        user.setUsRegestTime(ProjectUtil.getDBDate());
        return user.save();
    }

    public UsUser login(String phone, String password) {
        UsUser user = dao.findFirst("select * from us_user where us_mobile = ?", phone);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        } else {
            if (!user.getUsPass().equals(password)) {
                throw new RuntimeException("密码有误,请重试");
            } else {
                user.setUsLastTime(ProjectUtil.getDBDate());
                user.update();
            }
        }
        return user;
    }
}

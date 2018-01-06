package com.xiaomingming.api.action.ctrs;

import com.jfinal.aop.Before;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.action.ProjectValidator;
import com.xiaomingming.api.service.UserService;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.UsUser;

/**
 * Created by Administrator on 2017/12/29.
 */
public class UserCtr extends BaseCtr {

    private UserService service = new UserService();

    public void getRandemStr() {
        setSessionAttr("yzimg", ProjectUtil.getRandemStr());
        onOk(getSessionAttr("yzimg"));
    }

    @Before(ProjectValidator.class)
    public void sysRegist() {
        String phone = getPara("phone");
        String pass = getPara("pass");
        String yzimg = getPara("yzimg");
        String cur_yzimg = getSessionAttr("yzimg");
        if (!cur_yzimg.equals(yzimg)) {
            onErr("验证码有误");
            return;
        }
        try {
            resultMap.put("is_save", service.regist(phone, ProjectUtil.md5L(pass)));
            onOk(resultMap);
        } catch (Exception e) {
            logger.info(e);
            onErr("注册失败: " + e.getMessage());
        }
    }

    @Before(ProjectValidator.class)
    public void sysLogin() {
        String phone = getPara("phone");
        String pass = getPara("pass");
        String yzimg = getPara("yzimg");
        String cur_yzimg = getSessionAttr("yzimg");
        if (!cur_yzimg.equals(yzimg)) {
            onErr("验证码有误");
            return;
        }
        try {
            UsUser user = service.login(phone, ProjectUtil.md5L(pass));
            setSessionAttr("_currentUser", user);
            onOk(user);
        } catch (Exception e) {
            logger.info(e);
            onErr("登陆失败: " + e.getMessage());
        }
    }
}

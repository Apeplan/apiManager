package com.xiaomingming.api.action.ctrs;

import com.jfinal.aop.Before;
import com.xiaomingming.api.action.BaseCtr;
import com.xiaomingming.api.action.ProjectValidator;
import com.xiaomingming.api.service.UserService;
import com.xiaomingming.api.utils.ProjectUtil;
import com.xiaomingming.api.vo.UsUser;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/29.
 */
public class UserCtr extends BaseCtr {

    private UserService service = new UserService();
    private static HashMap<String, String> codes = new HashMap<>();

    public void getRandemStr() {
        try {
            String mobile = getPara("mobile");
            if (ProjectUtil.isEmpty(mobile) || mobile.length() != 11) {
                throw new RuntimeException("手机号有误,获取验证码失败");
            }
            codes.put(mobile, ProjectUtil.getRandemStr());
            onOk(codes.get(mobile));
        } catch (Exception e) {
            onErr(e.getMessage());
        }
    }

    @Before(ProjectValidator.class)
    public void sysRegist() {
        String phone = getPara("phone");
        String pass = getPara("pass");
        String yzimg = getPara("yzimg");
        String cur_yzimg = codes.get(phone);
        if (ProjectUtil.isEmpty(cur_yzimg) || !cur_yzimg.equals(yzimg)) {
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
        try {
            onOk(service.login(phone, ProjectUtil.md5L(pass)));
        } catch (Exception e) {
            logger.info(e);
            onErr("登陆失败: " + e.getMessage());
        }
    }
}

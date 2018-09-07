package com.xiaomingming.api.action;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/12/29.
 */
public class ProjectValidator extends Validator {

    /**
     * getActionKey(); - - - > /user/sysLogin
     * getActionMethod(); - - - > public void com.xiaomingming.api.action.ctrs.UserCtr.sysLogin()
     * getActionMethodName(); - - - > sysLogin
     * getControllerKey(); - - - > /user
     * getViewPath(); - - - > /views/user/
     *
     * @param ctr
     */
    protected void validate(Controller ctr) {
        switch (getControllerKey()) {
            case "/user":
                validateRequiredString("phone", "", "手机号不能为空");
                validateRequiredString("pass", "", "密码不能为空");
                break;
            case "/interface":
                validateRequiredString("fo_id", "", "请重新选择文件夹");
                validateRequiredString("in_id", "", "请重新选择接口");
                break;
            default:
                break;
        }
    }

    protected void handleError(Controller ctr) {
        ((BaseCtr) controller).logger.info("handleError - - > " + getActionKey());
    }

    @Override
    protected void addError(String errorKey, String errorMessage) {
        invalid = true;
        ((BaseCtr) controller).onErr(errorMessage);
    }
}

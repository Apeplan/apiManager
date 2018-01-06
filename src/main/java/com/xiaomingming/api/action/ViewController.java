package com.xiaomingming.api.action;

import com.jfinal.core.Controller;

public class ViewController extends Controller {

    public void index() {
        render("sysLogin.jsp");
    }

    public void home() {
        if (loginValidator()) {
            render("sysHome.jsp");
        }
    }

    public void splash() {
        if (loginValidator()) {
            render("404.jsp");
        }
    }

    public void work() {
        if (loginValidator()) {
            setSessionAttr("fo_id", getPara("fo_id"));
            render("sysWork.jsp");
        }
    }

    private boolean loginValidator() {
        if (getSessionAttr("_currentUser") == null) {
            index();
            return false;
        } else {
            return true;
        }
    }
}

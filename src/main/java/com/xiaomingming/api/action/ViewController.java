package com.xiaomingming.api.action;

import com.jfinal.core.Controller;

public class ViewController extends Controller {

    public void index() {
        render("/manager/dist/index.html");
    }

    public void about() {
        render("/view/aboutMe.html");
    }

}

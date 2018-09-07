package com.xiaomingming.api.action;

import com.jfinal.core.Controller;

public class ViewController extends Controller {

    public void index() {
        render("index.html");
    }

}

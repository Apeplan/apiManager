package com.xiaomingming.api.utils;

import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

/**
 * Created by Administrator on 2017/12/28.
 */
public class ProjectRenderFactory extends RenderFactory {
    public ProjectRenderFactory() {
        super();
        System.out.println("ProjectRenderFactory - - > init ");
    }

    @Override
    public Render getErrorRender(int errorCode) {
        System.out.println("ProjectRenderFactory - - > " + errorCode);
//        if (errorCode == 404) {
//            return new Render() {
//                @Override
//                public void setView(String view) {
//                    super.setView("/public/404.jsp");
//                }
//
//                public void render() {
//
//                }
//            };
//        }
        return super.getErrorRender(errorCode);
    }
}

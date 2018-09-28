package com.xiaomingming.api.utils;

import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.xiaomingming.api.action.ViewController;
import com.xiaomingming.api.action.ctrs.FoderCtr;
import com.xiaomingming.api.action.ctrs.InterfaceCtr;
import com.xiaomingming.api.action.ctrs.ProjectCtr;
import com.xiaomingming.api.action.ctrs.UserCtr;
import com.xiaomingming.api.vo._MappingKit;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * <p>
 * API引导式配置
 */
public class ProjectConfig extends JFinalConfig {

    /**
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     * <p>
     * 使用本方法启动过第一次以后，会在开发工具的 debug、run config 中自动生成
     * 一条启动配置，可对该自动生成的配置再添加额外的配置项，例如 VM argument 可配置为：
     * -XX:PermSize=64M -XX:MaxPermSize=256M
     */
    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/");
    }

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("api_config.txt");
        me.setViewType(ViewType.JSP);//设置当前的视图类型
        me.setDevMode(PropKit.getBoolean("devMode", false));
//        RenderManager.me().setRenderFactory(new ProjectRenderFactory());
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
//        me.setBaseViewPath("/views");
        me.add("/", ViewController.class);
        me.add("/user", UserCtr.class);
        me.add("/project", ProjectCtr.class);
        me.add("/foder", FoderCtr.class);
        me.add("/interface", InterfaceCtr.class);
    }

    public void configEngine(Engine me) {
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置 druid 数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        me.add(druidPlugin);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setShowSql(true);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        me.add(arp);
    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {

    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {

    }

    /**
     * 方便其它地方使用
     *
     * @return
     */
    public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }
}

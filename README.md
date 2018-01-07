# apiManager
>api接口请求及管理简易java后台

[项目演示地址](http://www.xiaomingming.top/vo/)  
>http://www.xiaomingming.top/vo/


### 引子
>无论BS还是CS,目前都注重分离试开发,交互格式json,此工具是解决常见接口请求,及参数储存,简单轻便,轻量级postmain

![项目结构](http://hcwy.xiaomingming.top/images/20180106/08e522bc-6b9d-4e3a-8dd8-361effe83a4c.png)
![登陆页面](http://hcwy.xiaomingming.top/images/20180106/4e644701-4a7a-4cbd-8b3f-c8baea114469.png)
![接口请求](http://hcwy.xiaomingming.top/images/20180106/474312ef-9ea1-4493-8c64-c9557969dea4.png)
![接口详情](http://hcwy.xiaomingming.top/images/20180106/75959d7c-2b16-4ecb-aba9-2a1f7ecc786c.png)
![接口详情](http://hcwy.xiaomingming.top/images/20180107/e4499c87-cbdf-4002-94f9-e1d5a02264e9.jpg)

### 架构

>后端: jfinal + jsp

>前端: vue + iview + jquery

### 文档(传送门)
[jfinal 在线文档](http://download.jfinal.com/download/3.3/jfinal-3.3-manual.pdf)  
[vue2.0 在线文档](https://cn.vuejs.org/v2/guide/)  
[iview 在线文档](https://www.iviewui.com/components/grid)  
[jquery 在线文档](http://www.w3school.com.cn/jquery/jquery_reference.asp)  

### 环境
> jdk1.7 + maven + tomcat7 + mysql

### 部署
> git clone 项目

> 数据库导入resources目录下api_sql脚本

> 添加你tomcat目录下lib包里的jsp-api.jar,servlet-api.jar,tomcat-api.jar至项目

> 修改resources目录下api_config.txt数据库配置文件

### 弊端
>请求内网地址,需在内网地址内,将此项目跑起来

>具体原因请看:InterfaceCtr.java的getSendResponse() - > java童鞋应该能秒懂







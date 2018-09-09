# 在线api接口管理系统
>api doc manager

[项目演示地址](http://www.xiaomingming.top)  
>https://www.xiaomingming.top:8443/


### 能解决的什么问题?
>无论BS还是CS,目前都注重前后端分离试开发,交互格式json,此工具是解决常见接口请求,及参数储存,简单轻便,轻量级postmain
### 为什么一定要用它?
>公司机密接口,岂能随便放第三方文档系统?自己写一套放公司测试服务器不行么?
### 适用人群
>所有开发者
### 项目优点
>适配移动端,让开发者能在公交上,地铁上,也能随时响应项目的号召,随时查看接口,测试接口

### 移动端
<div align=center>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_1.png" width="285" alt="111"/>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_2.png" width="285" alt="111"/>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_8.png" width="285" alt="111"/>
</div>
<div align=center>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_5.png" width="285" alt="111"/>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_6.png" width="285" alt="111"/>
<img src="http://pbstqnze8.bkt.clouddn.com/manager/mobile_7.png" width="285" alt="111"/>
</div>

### PC端

![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_1.png)
![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_2.png)
![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_3.png)
![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_4.png)
![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_5.png)
![img](http://pbstqnze8.bkt.clouddn.com/manager/pc_6.png)


### 数据结构

![接口详情](http://hcwy.xiaomingming.top/images/20180107/e4499c87-cbdf-4002-94f9-e1d5a02264e9.jpg)

### 架构
> 后端: jfinal + jsp

> 前端: webpack + vue2 + 妹子UI + jquery

### 文档
[jfinal 在线文档](http://download.jfinal.com/download/3.3/jfinal-3.3-manual.pdf)  
[vue2.0 在线文档](https://cn.vuejs.org/v2/guide/)  
[amazeui 在线文档](http://amazeui.org/)  
[jquery 在线文档](http://www.w3school.com.cn/jquery/jquery_reference.asp)  

### 环境
> 后端:jdk1.7 + maven + tomcat7 + mysql

> 前端:node + webpack + vue 

### 部署
> git clone 项目

> 数据库导入resources目录下api_sql脚本

> 添加你tomcat目录下lib包里的jsp-api.jar,servlet-api.jar,tomcat-api.jar至项目

> 修改resources目录下api_config.txt数据库配置文件

### 二开
> cd src\main\webapp\manager

> npm install

> npm run build or npm run dev (dev模式下注意启动端口号,不能与tomcat启动端口相同)

> your tomcat startup

### 弊端
>请求内网地址,需在内网地址内,将此项目跑起来

>具体原因请看:InterfaceCtr.java的getSendResponse() - > java童鞋应该能秒懂







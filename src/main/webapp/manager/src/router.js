import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: "*",
      redirect: "/project"
    },
    {
      meta: {title: '登陆', needlogin: false, needproject: false},
      path: "/login",
      component: (resolve) => require(['@/page/Login.vue'], resolve)
    },
    {
      meta: {title: '你的项目列表', needlogin: true, needproject: false},
      path: "/project",
      component: (resolve) => require(['@/page/Project.vue'], resolve)
    },
    {
      meta: {title: '主页', needlogin: false, needproject: true},
      path: "/index/:po_id",
      component: (resolve) => require(['@/page/Index.vue'], resolve)
    },
    {
      meta: {title: '控制台', needlogin: false, needproject: true},
      path: "/control/:in_id",
      component: (resolve) => require(['@/page/Control.vue'], resolve)
    },
  ]
})

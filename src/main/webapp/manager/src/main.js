// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import $ from 'jquery'
import AmazeVue from 'amaze-vue'
import model from '@/model'
import cache from '@/cache'
import constant from '@/constant'
import 'amaze-vue/dist/amaze-vue.css'
import '@/assets/css/animate.min.css'
import '@/assets/css/comm.css';

Vue.config.productionTip = false;

Vue.use(AmazeVue);

/**
 * 数组中是否存在(参数:对象)
 * @param arr
 * @param val
 * @returns {boolean}
 */
Vue.prototype.isExistByValue = (arr, val) => {
  try {
    for (var i = 0; i < arr.length; i++) {
      if (arr[i] == val) {
        return true;
      }
    }
    return false;
  } catch (e) {
    return false;
  }
};
/**
 * 从数组中移除(参数:下标)
 * @param arr
 * @param index
 */
Vue.prototype.removeByIndex = (arr, index) => {
  for (var i = 0; i < arr.length; i++) {
    if (i === index) {
      arr.splice(i, 1);
      break;
    }
  }
};
/**
 * 从数组中移除(参数:对象)
 * @param arr
 * @param val
 */
Vue.prototype.removeByValue = (arr, val) => {
  for (var i = 0; i < arr.length; i++) {
    if (arr[i] == val) {
      arr.splice(i, 1);
      break;
    }
  }
};

let isEmpty = (str) => {
  if (str === "null" || str === "" || str === 0 || str === "0" || str === null || str === false || typeof str === 'undefined' || str.length === 0) {
    return true;
  }
  return false;
};
/**
 * 全局非空
 */
Vue.prototype.isEmpty = isEmpty;
/**
 * 全局applocation
 */
Vue.prototype._value = constant;
/**
 * 全局model层
 */
Vue.prototype._model = model;
/**
 * 全局缓存对像
 */
Vue.prototype._cache = cache;

/**
 * 全局页面跳转拦截
 */
router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title;
  if (to.meta.needlogin && constant.userinfo === null || to.meta.needproject && constant.projectinfo === null) {
    if (to.meta.needproject) {
      router.push('/project');
      return;
    }
    if (to.meta.needlogin) {
      var userinfo = cache.get('userinfo');
      if (!isEmpty(userinfo)) {
        constant.userinfo = userinfo;
        next();
      } else {
        router.push('/login');
      }
      return;
    }
  } else {
    next();
  }
});

window.onload = () => {
  new Vue({
    el: '#app',
    router,
    render: (x) => x(App)
  });

}

/**
 * Created by Administrator on 2018/8/10.
 */
export default {
  /**
   * 总容量5M,存入缓存，支持字符串类型、json对象的存储,页面关闭后依然有效 ie7+都有效
   * @time 数字 缓存有效时间（秒） 默认一周
   * */
  put (key, stringVal, time) {
    try {
      if (!localStorage) {
        return false;
      }
      if (!time || isNaN(time)) {
        time = 60 * 60 * 24 * 7;
      }
      var cacheExpireDate = (new Date() - 1) + time * 1000;
      var cacheVal = {val: stringVal, exp: cacheExpireDate};
      localStorage.setItem(key, JSON.stringify(cacheVal));//存入缓存值
    } catch (e) {
    }
  },
  /**获取缓存*/
  get (key) {
    try {
      if (!localStorage) {
        return false;
      }
      var cacheVal = localStorage.getItem(key);
      var result = JSON.parse(cacheVal);
      var now = new Date() - 1;
      if (!result) {
        return "";
      }//缓存不存在
      if (now > result.exp) {//缓存过期
        this.remove(key);
        return "";
      }
      return result.val;
    } catch (e) {
      this.remove(key);
      return "";
    }
  }, /**移除缓存，一般情况不手动调用，缓存过期自动调用*/
  remove (key) {
    if (!localStorage) {
      return false;
    }
    localStorage.removeItem(key);
  }, /**清空所有缓存*/
  clear () {
    if (!localStorage) {
      return false;
    }
    localStorage.clear();
  }
}

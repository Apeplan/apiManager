/**
 * Created by Administrator on 2018/6/5.
 */

/**
 * 当前环境
 * @type {boolean}
 */
const isProduction = process.env.NODE_ENV === 'production'
/**
 * 工程url,
 * npm run dev    时,数据请求地址为http://127.0.0.1:8080/
 * npm run build  时,数据请求地址为相对路径,可不设置
 */
const ROOT = isProduction ? '' : 'http://127.0.0.1:8080/';

export default {
  getRequest(url, req, Callback) {
    console.info(req);
    $.ajax({
      url: url,
      type: 'POST',
      data: req,
      timeout: 5000,
      error() {
        if (Callback instanceof Function) {
          Callback('网络异常,请重试', true);
        }
      },
      success(res) {
        console.info(res);
        if (Callback instanceof Function) {
          if (res.status !== 0) {
            Callback(res.msg, true);
          } else {
            Callback(res.data, false);
          }
        }
      }
    });
  },
  urls: {
    /**
     * 获取验证码
     */
    user_getRandemStr: ROOT + "user/getRandemStr",
    /**
     * 用户注册
     */
    user_sysRegist: ROOT + "user/sysRegist",
    /**
     * 用户登陆
     */
    user_sysLogin: ROOT + "user/sysLogin",
    /**
     * 项目列表
     */
    project_getProjects: ROOT + "project/getProjects",
    /**
     * 添加/编辑项目
     */
    project_editProject: ROOT + "project/editProject",
    /**
     * 删除项目
     */
    project_delProject: ROOT + "project/delProject",
    /**
     * 项目分享
     */
    project_shardProject: ROOT + "project/shardProject",
    /**
     * 文件夹列表
     */
    foder_getFoders: ROOT + "foder/getFoders",
    /**
     * 新建/编辑 文件夹
     */
    foder_addFoder: ROOT + "foder/addFoder",
    /**
     * 删除文件夹
     */
    foder_delFoder: ROOT + "foder/delFoder",
    /**
     * 搜索文件夹&搜索接口
     */
    foder_searchFoders: ROOT + "foder/searchFoders",
    /**
     * 接口内容
     */
    interface_getDetail: ROOT + "interface/getDetail",
    /**
     * 接口发送
     */
    interface_getSendResponse: ROOT + "interface/getSendResponse",
    /**
     * 接口保存
     */
    interface_saveInterface: ROOT + "interface/saveInterface",
    /**
     * 删除接口
     */
    interface_deleteDoc: ROOT + "interface/deleteDoc",
  }
}

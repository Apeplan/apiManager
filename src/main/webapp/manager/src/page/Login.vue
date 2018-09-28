<template>
  <div v-cloak class="bg w100 h100">
    <div class="c-layout am-u-sm-10 am-u-md-4 zoomInUp animated">
      <div class="c-title">{{isRegist? '注册':'登陆'}}</div>
      <am-row class="relative">
        <am-input type="number" v-model="username" placeholder="输入手机号码" class="c-user"></am-input>
        <i v-if="!isEmpty(username)" type="close" class="am-icon-close c-clear" @click="username = ''"></i>
      </am-row>
      <am-row class="relative">
        <am-input type="password" v-model="password" placeholder="在此输入密码" class="c-pass"></am-input>
        <i v-if="!isEmpty(password)" type="close" class="am-icon-close c-clear" @click="password = ''"></i>
      </am-row>
      <am-row class="relative zoomInDown animated" v-if="isRegist">
        <am-input type="text" v-model="msgcode" placeholder="输入验证码" class="c-pass"></am-input>
        <am-button :round="true" class="c-code" @click="onCode" :disabled="codeDis">{{codeBtn}}</am-button>
      </am-row>
      <am-button class="c-submit" @click="onSubmit">{{isRegist? '确认注册':'确认登陆'}}</am-button>
      <div class="c-regist" @click="isRegist = !isRegist">{{isRegist? '已有帐户,马上登陆':'没有帐户,马上注册'}}</div>
      <div class="c-forget">忘记密码</div>
    </div>
    <div class="c-version w100" onclick="window.location.href = 'about'">@关于xiao明</div>
    <am-modal :is-show.sync="isShowCode" class="am-u-sm-12 am-u-md-4 am-center">
      <am-modal-header class="orange alignC">个人项目,请君谅解</am-modal-header>
      <am-modal-body>
        <div style="font-size: 17px">您的验证码是[{{serviceCode}}],有效期为60秒</div>
      </am-modal-body>
      <am-modal-footer>
        <am-button @click="isShowCode = false">关闭</am-button>
      </am-modal-footer>
    </am-modal>
  </div>
</template>


<script>

  let _this = null;

  export default {
    data() {
      return {
        isRegist: false,
        isShowCode: false,
        username: '',
        password: '',
        msgcode: '',
        serviceCode: '',
        codeBtn: '获取验证码',
        codeDis: false,
      }
    },
    methods: {
      init() {

      },
      reset() {
        _this.password = '';
        _this.msgcode = '';
        _this.serviceCode = '';
        _this.isRegist = false;
      },
      isMobile(text) {
        return /^1\d{10}$/.test(text);
      },
      onSubmit() {
        if (!_this.isMobile(_this.username)) {
          _this.$notify({
            message: '请正确输入手机号',
            type: 'warning'
          });
          return;
        }
        if (!/^[A-Za-z\d]+$/.test(_this.password)) {
          _this.$notify({
            message: '请正确输入密码',
          });
          return;
        }
        if (_this.isRegist) {
          if (_this.isEmpty(_this.msgcode)) {
            _this.$notify({
              message: '请输入验证码',
            });
            return;
          }
          _this._model.getRequest(_this._model.urls.user_sysRegist, {
            phone: _this.username, pass: _this.password, yzimg: _this.msgcode,
          }, (res, isErr) => {
            if (isErr) {
              _this.$notify({
                message: res,
                type: 'error'
              });
            } else {
              _this.$notify({
                message: '注册成功,请登陆',
                type: 'success'
              });
              _this.reset();
            }
          })
        } else {
          _this._model.getRequest(_this._model.urls.user_sysLogin, {
            phone: _this.username, pass: _this.password
          }, (res, isErr) => {
            if (isErr) {
              _this.$notify({
                message: res,
                type: 'error'
              });
            } else {
              _this.$notify({
                message: '登陆成功',
                type: 'success'
              });
              _this.reset();
              _this._cache.put('userinfo', res);
              _this.$router.push('/project');
            }
          })
        }

      },
      onCode() {
        if (!_this.isMobile(_this.username)) {
          _this.$notify({
            message: '请正确输入手机号',
            type: 'warning'
          });
          return;
        }
        _this._model.getRequest(_this._model.urls.user_getRandemStr, {mobile: _this.username}, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.serviceCode = res;
            _this.isShowCode = true;
            _this.onTime();
          }
        })
      },
      onTime() {
        var timer = 60;
        _this.codeDis = true;
        _this.i_thread = setInterval(() => {
          if (timer === 0) {
            _this.codeDis = false;
            _this.codeBtn = '获取验证码';
            clearInterval(_this.i_thread);
          } else {
            _this.codeBtn = timer + '秒后重发';
            timer -= 1;
          }
        }, 1000);
      },
    },
    watch: {},
    props: [],
    components: {},
    computed: {},
    activated() {
      _this = this;
      _this.init();
      _this.$nprogress.end();
    },
    deactivated() {
      _this.reset();
    }
  }
</script>


<style scoped>
  .am-modal-footer button {
    width: 100px;
    float: none !important;
    display: block !important;
    margin-left: auto !important;
    margin-right: auto !important;
  }

  .bg {
    background: url(https://cdn.apizza.net/home/assets/img/bg/img-bg-26.jpg) 50% 50% no-repeat;
  }

  .c-code {
    position: absolute;
    right: 6px;
    top: 13px;
  }

  .c-layout {
    transform: translate(-50%, -50%); /*自己的50% */
    text-align: center;
    position: absolute;
    left: 50%; /* 定位父级的50% */
    top: 50%;
  }

  .c-title {
    color: black;
    font-size: 28px;
    line-height: 60px;
  }

  .c-user, .c-pass, .c-submit {
    text-align: center;
    line-height: 22px;
    display: block;
    border-radius: 30px;
    margin: 8px 0;
    font-size: 18px;
  }

  .c-user, .c-pass {
    background-color: rgba(255, 255, 255, .75);
    color: black;
  }

  .c-clear {
    position: absolute;
    cursor: pointer;
    font-size: 18px;
    right: 15px;
    top: 15px;
  }

  .c-user {

  }

  .c-pass {

  }

  .c-submit {
    width: 100%;
  }

  .c-forget, .c-version, .c-regist {
    line-height: 30px;
    font-size: 13px;
    color: white;
    cursor: pointer;
  }

  .c-forget:hover, .c-regist:hover {
    color: #ff4530;
  }

  .c-version {
    text-align: center;
    position: fixed;
    bottom: 20px;
    cursor: default;
  }

  .c-regist {
    margin-top: 15px;
  }

</style>

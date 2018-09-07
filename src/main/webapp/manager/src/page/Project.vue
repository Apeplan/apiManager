<template>
  <div v-cloak class="w100">
    <div class="header">
      <am-row class="white am-u-sm-10 am-u-md-3 am-center alignC">
        <am-col class="c-title">项目列表</am-col>
        <am-col>
          <am-button @click="def = {};isEdit = true">创建项目</am-button>
        </am-col>
      </am-row>
    </div>
    <am-container>
      <div v-for="item in projects" class="am-fl am-u-sm-12 am-u-md-3 alignC white-bg">
        <div class="i-layout relative">
          <img class="i-icon" src="@/assets/img/icon-computer.png"/>
          <div class="i-name">{{item.prName}}</div>
          <div class="i-info">{{isEmpty(item.prInfo) ? '暂无简介':item.prInfo}}</div>
          <i type="close" class="i-close absolute orange am-icon-close" @click="def = item;isDel = true"></i>
          <div class="i-footer relative">
            <am-button class="i-action" @click="_value.projectinfo = item;$router.push('/index/'+ item.id)">
              进入控制台
            </am-button>
            <span class="i-setting" @click="def = {id:item.id,prName:item.prName,prInfo:item.prInfo};isEdit = true">
               <am-icon type="cog"></am-icon>
            </span>
          </div>
        </div>
      </div>
      <div class="am-cf"></div>
    </am-container>
    <am-modal :is-show.sync="isEdit" class="am-u-sm-12 am-u-md-4 am-center">
      <am-modal-header>编辑项目</am-modal-header>
      <am-modal-body>
        <am-form>
          <am-input-group block>
            <am-input placeholder="项目名称" v-model="def.prName"></am-input>
          </am-input-group>
          <am-input-group block>
            <am-input type="textarea" placeholder="项目描述(选填)" v-model="def.prInfo"></am-input>
          </am-input-group>
        </am-form>
      </am-modal-body>
      <am-modal-footer>
        <am-button @click="isEmpty(def.prName) ? $notify({
            message: '请输入项目名称',
            type: 'warning'
          }):isEdit = false;onEdit(def)">保存
        </am-button>
      </am-modal-footer>
    </am-modal>
    <am-modal :is-show.sync="isDel" class="am-u-sm-12 am-u-md-4 am-center">
      <am-modal-header class="orange alignC">即将删除{{def.prName}}</am-modal-header>
      <am-modal-body>
        <div style="font-size: 17px">如果您点击删除,你的项目和项目里面的API都将被删除,请谨慎.</div>
      </am-modal-body>
      <am-modal-footer>
        <am-button @click="isDel = false;onDel(def)">删除</am-button>
      </am-modal-footer>
    </am-modal>
  </div>
</template>

<script>
  let _this = null;

  export default {
    data() {
      return {
        isEdit: false,
        isDel: false,
        def: {},
        projects: []
      }
    },
    methods: {
      init() {
        _this.$nprogress.start({
          loading: true
        });
        _this._model.getRequest(_this._model.urls.project_getProjects, {
          us_id: _this._value.userinfo.id
        }, (res, isErr) => {
          _this.$nprogress.end();
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.projects = res;
          }
        })
      },
      reset() {

      },
      onDel(item) {
        item.us_id = _this._value.userinfo.id;
        _this._model.getRequest(_this._model.urls.project_delProject, item, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.$notify({
              message: '操作成功',
              type: 'success'
            });
            _this.init();
          }
        })
      },
      onEdit(item) {
        item.us_id = _this._value.userinfo.id;
        _this._model.getRequest(_this._model.urls.project_editProject, item, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.$notify({
              message: '操作成功',
              type: 'success'
            });
            _this.init();
          }
        })
      },
    },
    watch: {},
    props: [],
    components: {},
    computed: {},
    created() {
      this.$nextTick(() => {
        _this = this;
      })
    },
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

  .am-modal-footer {
    text-align: center;
  }

  .am-modal-footer button {
    width: 100px;
    float: none !important;
    display: block !important;
    margin-left: auto !important;
    margin-right: auto !important;
  }

  .header {
    background: url(https://cdn.apizza.net/home/assets/img/bg/bg-parallax-5.jpg);
    padding: 100px 0;
    min-height: 320px;
  }

  .c-title {
    font-size: 35px;
    line-height: 50px;
    margin-bottom: 20px;
  }

  .i-layout {
    border: 1px solid #DDDDDD;
    cursor: pointer;
    margin: 20px 0;
    padding: 10px;
  }

  .i-layout .i-name {
    color: #333333;
    font-size: 16px;
    line-height: 35px;
  }

  .i-layout .i-info {
    color: #646464;
    text-align: left;
    text-indent: 2rem;
    height: 40px;
    overflow-y: auto;
    padding: 0 5px;
  }

  .i-layout .i-icon {
    width: 60px;
  }

  .i-layout .i-footer {
    margin-top: 10px;
    font-size: 12px;
  }

  .i-layout .i-footer .i-setting {
    display: inline-block;
    width: 28px;
    height: 28px;
    line-height: 28px;
    background-color: #5F6467;
    color: white;
    position: absolute;
    margin-left: 5px;
    border-radius: 3px;
    cursor: pointer;
  }

  .i-layout .i-footer .i-action {
    font-size: 12px;
  }

  .i-layout .i-close {
    display: none;
    top: 5px;
    right: 10px;
  }

  .i-layout:hover > .i-close {
    display: inline-block;
  }

</style>

<template>
  <div v-cloak class="w100 h100">
    <div class="am-layout h100">
      <div class="am-layout horizontal w100 h100">
        <div class="am-aside h100">
          <div class="a-layout">
            <div class="a-header slide-bg">
              <div class="a-mobile">{{_value.userinfo.usName}}</div>
              <div class="a-project">{{_value.projectinfo.prName}}<i class="am-icon-exchange" @click="$router.push('/project')"></i></div>
            </div>
            <div class="a-search slide-bg">
              <am-input-group block class="relative">
                <am-input-label transparent slot="prepend">
                  <am-icon type="search"></am-icon>
                </am-input-label>
                <am-input placeholder="搜索名称/URL" v-model="search"></am-input>
                <i v-if="!isEmpty(search)" class="am-icon-close a-clear" @click="search = ''"></i>
              </am-input-group>
            </div>
            <div class="a-util slide-bg">
              <i class="am-icon-refresh" @click="init"></i>
              <i class="am-icon-list" @click="onReset"></i>
              <i class="am-icon-folder" @click="def = {id: 0, name: ''};isShowEdit = true"></i>
              <i class="am-icon-file-o" @click="openTab({name: '新建api', isshow: true, id: 0})"></i>
            </div>
          </div>
          <am-scrollbar class="a-menu" ref="vmenu">
            <div style="height: 153px;"></div>
            <div :id="item.name" class="fo-layout" v-for="item in menus">
              <div class="fo-main slide-bg" @click="onOpen(item)">
                <am-icon class="icon-0" :type="item.isOpen ? 'folder-open-o':'folder-o'"></am-icon>
                <i class="icon-1 am-icon-edit" @click.stop="def = item;isShowEdit = true"></i>
                <i class="icon-2 am-icon-trash" @click.stop="onFoDel(item)"></i>
                <div class="fo-name">{{item.name}}</div>
              </div>
              <div class="fo-sub">
                <div class="sub-li" v-for="subitem in item.ins" @click="openTab(subitem)">
                  <i class="am-icon-file-text-o icon-0"></i>
                  <i class="am-icon-trash icon-2" @click.stop="c_subitem = subitem;c_ins = item.ins;isShowDel = true"></i>
                  {{subitem.name}}
                </div>
              </div>
            </div>
          </am-scrollbar>
        </div>
        <div class="am-main h100">
          <div class="tb-layout slide-bg">
            <template v-for="item in tabs">
              <span :class="tabstyle(item)" @click="openTab(item)">
                  <span class=" t-title">{{item.name}}</span>
                  <span @click.stop="closeTab(item)" class="am-icon-close t-close"></span>
              </span>
            </template>
          </div>
          <div class="fm-layout">
            <template v-for="item in tabs">
              <v-control v-show="item.isshow" :in_id="item.id" @onChange="onChange"></v-control>
            </template>
          </div>
        </div>
      </div>
    </div>
    <span @click="onSlide(isSlide = !isSlide)" class="am-btn am-btn-sm orange-bg am-icon-bars am-show-sm-only toggle-slide-btn"><span class="am-sr-only">侧栏导航</span></span>
    <am-modal :is-show.sync="isShowDel" class="am-u-sm-12 am-u-md-4 am-center">
      <am-modal-header class="orange alignC">请确认</am-modal-header>
      <am-modal-body>
        <div class="alignC">您确定要删除[{{c_subitem.name}}]吗</div>
      </am-modal-body>
      <am-modal-footer>
        <am-button @click="isShowDel = false;onInDel(c_subitem,c_ins)">确定</am-button>
        <am-button color="success" @click="isShowDel = false">取消</am-button>
      </am-modal-footer>
    </am-modal>
    <am-modal :is-show.sync="isShowEdit" class="am-u-sm-12 am-u-md-4 am-center">
      <am-modal-header class="orange alignC">{{isEmpty(def.id) ? '新建文件夹':'编辑文件夹'}}</am-modal-header>
      <am-modal-body>
        <div class="alignC">
          <am-input placeholder="请输入文件夹名称" v-model="def.name"></am-input>
        </div>
      </am-modal-body>
      <am-modal-footer>
        <am-button @click="onEditForder(def)">确定</am-button>
      </am-modal-footer>
    </am-modal>
  </div>
</template>

<script>
  import control from '@/page/Control';

  let _this = null;
  export default {
    data() {
      return {
        def: {id: 0, name: ''},
        c_ins: [],
        c_subitem: {},
        isShowDel: false,
        isShowEdit: false,
        search: '',
        isSlide: false,
        tabs: [{
          name: '新建api', isshow: true, id: 0
        }],
        menus: [],
      }
    },
    methods: {
      init() {
        _this.menus = [];
        _this.getFooders();
        _this.$refs.vmenu.scrollTop(0);
      },
      reset() {

      },
      onEditForder(item) {
        console.info(item);
        if (_this.isEmpty(item.name)) {
          _this.$message({
            title: '参数异常',
            type: 'warning',
            message: '请正确填入文件夹名称',
            closeable: true
          })
        } else {
          _this._model.getRequest(_this._model.urls.foder_addFoder, {
            us_id: _this._value.userinfo.id, pr_id: _this.$route.params.po_id, fo_id: item.id, fo_name: item.name
          }, (res, isErr) => {
            if (isErr) {
              _this.$notify({
                message: res,
                type: 'error'
              });
            } else {
              _this.isShowEdit = false;
              _this.getFooders();
              _this.$message({
                title: '操作成功',
                message: ' ',
                type: 'success',
                closeable: true
              });
            }
          })
        }
      },
      onFoDel(item) {
        _this._model.getRequest(_this._model.urls.foder_delFoder, {
          us_id: _this._value.userinfo.id, pr_id: _this.$route.params.po_id, fo_id: item.id
        }, (res, isErr) => {
          if (isErr) {
            _this.$message({
              title: '操作异常',
              type: 'warning',
              message: res,
              closeable: true
            });
          } else {
            _this.removeByValue(_this.menus, item);
            _this.$message({
              title: '操作成功',
              message: ' ',
              type: 'success',
              closeable: true
            });
          }
        })
      },
      onInDel(item, arr) {
        _this._model.getRequest(_this._model.urls.interface_deleteDoc, {
          us_id: _this._value.userinfo.id, in_id: item.id
        }, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            console.info(arr.length);
            _this.removeByValue(arr, item);
            console.info(arr.length);
            _this.$message({
              title: '操作成功',
              message: ' ',
              type: 'success',
              closeable: true
            });
          }
        })
      },
      onChange(item) {
        for (var i = 0; i < _this.tabs.length; i++) {
          if (_this.tabs[i].isshow == true) {
            _this.tabs[i].name = item.inName;
            _this.tabs[i].id = item.id;
            for (var j = 0; j < _this.menus.length; j++) {
              if (_this.menus[j].id == item.foId) {
                _this.getFooders(_this.menus[j]);
                return;
              }
            }
            return;
          }
        }
      },
      getFooders(menu) {
        _this._model.getRequest(_this._model.urls.foder_getFoders, {
          us_id: _this._value.userinfo.id, pr_id: _this.$route.params.po_id
        }, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.menus = res;
            if (typeof menu == 'object') {
              _this.onOpen(menu);
              _this.$refs.vmenu.scrollTop(0);
            }
          }
        })
      },
      searchFoders() {
        _this._model.getRequest(_this._model.urls.foder_searchFoders, {
          us_id: _this._value.userinfo.id, pr_id: _this.$route.params.po_id, search: _this.search
        }, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.menus = res;
            _this.$refs.vmenu.scrollTop(0);
          }
        })
      },
      tabstyle(item) {
        return item.isshow ? 't-group t-group-active' : 't-group';
      },
      closeTab(item) {
        _this.removeByValue(_this.tabs, item);
        this.openTab(_this.tabs[_this.tabs.length - 1]);
      },
      openTab(item) {
        try {
          item.isshow = true;
        } catch (e) {
          return;
        }
        var isexist = false;
        for (var i = 0; i < _this.tabs.length; i++) {
          if (_this.tabs[i].name == item.name) {
            isexist = true;
            _this.tabs[i].isshow = true;
          } else {
            _this.tabs[i].isshow = false;
          }
        }
        _this.onSlide(_this.isSlide = false);
        if (!isexist) {
          _this.tabs.push(item);
          let instance = _this.$loading();
          setTimeout(() => {
            instance.close();
          }, 1000);
        }
      },
      onReset() {
        var isOpen = false;
        for (var i = 0; i < _this.menus.length; i++) {
          var item = _this.menus[i];
          if (item.isOpen) {
            isOpen = true;
            break;
          }
        }
        if (isOpen) {
          _this.$refs.vmenu.scrollTop(0);
        }
        for (var i = 0; i < _this.menus.length; i++) {
          var item = _this.menus[i];
          var $layout = $('#' + item.name).find('.fo-sub');
          if ($layout && item.ins.length > 0) {
            if ($layout) {
              if (isOpen) {
                item.isOpen = false;
                $layout.slideUp();
              } else {
                item.isOpen = true;
                $layout.slideDown();
              }
            }
          }
        }
      },
      onOpen(item) {
        var $layout = $('#' + item.name).find('.fo-sub');
        if ($layout && item.ins.length > 0) {
          if ($layout) {
            item.isOpen ? $layout.slideUp() : $layout.slideDown();
            item.isOpen = !item.isOpen;
          }
        }
      },
      onSlide(open) {
        if ($(window).width() < 641) {
          if (open) {
            $('.am-aside, .a-layout').animate({left: 0}, 200, () => {
              $('.a-menu').animate({left: 0}, 200);
            });
          } else {
            $('.a-menu, .am-aside, .a-layout').animate({left: -275}, 200);
          }
        }
      },
    },
    watch: {
      search(val) {
        if (_this.isEmpty(val)) {
          _this.getFooders();
        } else {
          _this.searchFoders();
        }
      }
    },
    props: [],
    components: {'v-control': control},
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
    margin: 5px 10px !important;
  }

  .a-layout {
    width: 275px;
    color: white;
    background-color: #2E2E2E;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1;
  }

  .a-menu {

  }

  .fm-layout {
    overflow: hidden;
    flex: 1;
  }

  .tb-layout {
    font-size: 12px;
    right: 0;
    color: white;
    line-height: 25px;
  }

  .tb-layout .t-group {
    display: inline-block;
    padding: 0 23px 0 15px;
    margin-left: 10px;
    position: relative;
    margin-top: 11px;
    cursor: pointer;
  }

  .tb-layout .t-group:hover, .tb-layout .t-group-active {
    border-radius: 8px 8px 0 0;
    -webkit-border-radius: 8px 8px 0 0;
    background: #f5f5f5;
    color: #424242;
  }

  .tb-layout .t-group:hover > .t-close {
    display: block;
  }

  .tb-layout .t-title {
  }

  .tb-layout .t-close {
    display: none;
    width: 11px;
    height: 11px;
    cursor: pointer;
    position: absolute;
    right: 6px;
    top: 0;
  }

  .fo-layout {
    position: relative;
    line-height: 40px;
    cursor: pointer;
  }

  .fo-layout .fo-sub {
    display: none;
  }

  .fo-layout .fo-sub .sub-li {
    padding: 0 30px;
  }

  .fo-layout .fo-sub .sub-li:hover {
    background-color: rgba(255, 255, 255, .05);
  }

  .fo-layout .fo-sub .sub-li > .icon-0 {
    display: none;
  }

  .fo-layout .fo-sub .sub-li:hover > .icon-2, .fo-layout .fo-sub .sub-li:hover > .icon-0 {
    display: block;
  }

  .fo-layout .fo-main:hover {
    background-color: #616161;
  }

  .fo-layout .fo-main:hover > .icon-2, .fo-layout .fo-main:hover > .icon-1 {
    display: block;
  }

  .fo-layout .fo-main .fo-name {
    margin-left: 30px;
    margin-right: 65px;
  }

  .icon-1 {
    display: none;
    right: 44px;
  }

  .icon-2 {
    display: none;
    right: 20px;
  }

  .icon-0 {
    left: 10px;
  }

  .icon-1, .icon-2, .icon-0 {
    position: absolute;
    color: #BDBDBD;
  }

  .a-clear {
    position: absolute;
    cursor: pointer;
    font-size: 15px;
    right: 10px;
    top: 5px;
    z-index: 5;
  }

  .am-input-group {
    margin: 0;
  }

  .a-header {
    padding: 25px 10px 10px;
  }

  .a-util {
    text-align: right;
    color: #BDBDBD;
    padding: 5px 15px;
    margin-bottom: 1px;
  }

  .a-util i {
    text-align: center;
    width: 20px;
    cursor: pointer;
    font-size: 12px;
    right: 0;
  }

  .a-search {
    color: #BDBDBD;
    margin: 1px 0;
    padding: 5px 10px;
  }

  .a-search input {
    border-radius: 30px !important;
    padding-left: 12% !important;
  }

  .a-project {
    position: relative;
    font-size: 16px;
    line-height: 25px;
  }

  .a-util i:hover, .a-project i:hover, .icon-1:hover, .icon-2:hover {
    color: #ff4530;
  }

  .a-project i {
    color: #BDBDBD;
    position: absolute;
    text-align: center;
    width: 30px;
    cursor: pointer;
    font-size: 12px;
    right: 0;
  }

  .a-mobile {
    font-size: 13px;
  }

  @media only screen and (min-width: 641px) {
    .am-aside {
      position: relative;
    }

    .am-aside, .a-layout, .a-menu {
      width: 275px;
    }
  }

  @media only screen and (max-width: 640px) {
    .am-aside {
      position: fixed;
      z-index: 1;
    }

    .am-aside, .a-layout, .a-menu {
      left: -275px;
    }
  }

  .toggle-slide-btn {
    color: white;
    position: fixed;
    top: 3px;
    right: 3px;
    border-radius: 0;
  }

  .am-layout {
    display: flex;
    flex-direction: column;
    background-color: #f5f5f5;
    padding: 0;
  }

  .horizontal {
    flex-direction: row;
  }

  .am-aside {
    z-index: 5;
    width: 275px;
    color: white;
    background-color: #2E2E2E;
  }

  .am-main {
    display: flex;
    flex-direction: column;
    flex: 1;
    background-color: #f5f5f5;
  }

  .slide-bg {
    background-color: #424242;
  }

  iframe {
    border: none;
    width: 100%;
    height: 100%;
  }
</style>

<template>
  <div class="w100 h100" v-cloak>
    <ol class="am-breadcrumb" v-if="_value.forderinfo != null">
      <li>{{_value.projectinfo.prName}}</li>
      <li>{{_value.forderinfo.name}}</li>
      <li class="am-active">{{info.in_name}}</li>
    </ol>
    <div class="h100" style="overflow-y: scroll">
      <div class="am-panel am-panel-default">
        <header class="am-panel-hd">
          <h3 class="am-panel-title">参数配置</h3>
        </header>
        <div class="am-panel-bd">
          <am-input-group>
            <am-input-label slot="prepend">接口路径</am-input-label>
            <am-input placeholder="输入请求接口url" v-model="info.in_url"></am-input>
          </am-input-group>
          <am-input-group class="am-u-md-6 pad-0">
            <am-input-label slot="prepend">接口名称</am-input-label>
            <am-input placeholder="输入请求接口名" v-model="info.in_name"></am-input>
          </am-input-group>
          <am-button-group class="pad-0 am-fr">
            <am-button :round="true" color="success" @click="params.push({})">增加参数</am-button>
            <am-button color="warning" @click="onSave">保存接口</am-button>
            <am-button color="secondary" @click="onSend">接口发送</am-button>
            <div class="am-cf"></div>
          </am-button-group>
          <div class="am-cf"></div>
          <div class="am-show-sm-only" style="margin-bottom: 1.25rem;"></div>
          <div class="para-layout" v-for="item in params">
            <am-input-group class="am-u-sm-5 am-u-md-2 pad-0 mar-0">
              <am-input class="cus-boder boderR-0" placeholder="[参数键]例:userid " v-model="item.pa_key"></am-input>
            </am-input-group>
            <am-input-group class="am-u-sm-7 am-u-md-4 pad-0 mar-0">
              <am-input class="cus-boder" placeholder="[参数值]例:9527 " v-model="item.pa_value"></am-input>
            </am-input-group>
            <am-input-group class="am-u-sm-8 am-u-md-4 pad-0">
              <am-input class="cus-boder-t" placeholder="[参数说明]" v-model="item.pa_example"></am-input>
            </am-input-group>
            <div class="am-u-sm-4 am-u-md-2 pad-0 alignR">
              <am-button color="danger" class="btn-del" @click="removeByValue(params,item)">删除参数</am-button>
            </div>
            <div class="am-cf"></div>
          </div>
        </div>
      </div>
      <am-panel>
        <am-panel-header title="请求结果" :title-level=4></am-panel-header>
        <am-panel-body>
          <pre v-html="resetJson(responseStr)"></pre>
        </am-panel-body>
        <am-panel-footer>
          <div class="am-cf">
            <am-button-group class="pad-0 am-fr">
              <am-button :round="true" class="inline-pad" color="danger" @click="onSetErr">设为失败示例</am-button>
              <am-button class="inline-pad" color="success" @click="onSetOK">设为成功示例</am-button>
              <am-button class="inline-pad" color="secondary" @click="copyToClipboard(responseStr)">复制结果</am-button>
              <div class="am-cf"></div>
            </am-button-group>
          </div>
        </am-panel-footer>
      </am-panel>
      <am-panel>
        <am-panel-body style="padding: 0">
          <v-editor ref="md" :initData="info.in_doc" :editorId="'markdown-editor-' + in_id"></v-editor>
        </am-panel-body>
      </am-panel>
      <div class="pad-0">
        <am-panel color="danger" ref="resErr">
          <am-panel-header title="请求失败示例" :title-level=4></am-panel-header>
          <am-panel-body>
            <pre v-html="resetJson(info.in_response_err)"></pre>
          </am-panel-body>
        </am-panel>
      </div>
      <div class="pad-0">
        <am-panel color="success" ref="resErr">
          <am-panel-header title="请求成功示例" :title-level=4></am-panel-header>
          <am-panel-body>
            <pre v-html="resetJson(info.in_response_ok)"></pre>
          </am-panel-body>
        </am-panel>
      </div>
      <div class="am-cf"></div>
      <div style="height: 30px;"></div>
      <am-modal :is-show.sync="isSelectForder" class="am-u-sm-12 am-u-md-4 am-center">
        <am-modal-header class="orange alignC">保存至</am-modal-header>
        <am-modal-body>
          <am-select v-model="def" :options="forderArr" :search="true" class="dis-bok mar-0"></am-select>
        </am-modal-body>
        <am-modal-footer>
          <am-button @click="isSelectForder = false;onSelectForder()">确定</am-button>
        </am-modal-footer>
      </am-modal>
    </div>
  </div>
</template>

<script>
  import editor from '@/compoents/EditorMD';

  export default {
    data() {
      return {
        def: [],
        isSelectForder: false,
        forderArr: [],
        responseStr: '',
        info: {in_url: 'http://', params: [], in_response_ok: '', in_response_err: '', in_name: '', fo_id: 0},
        params: [],
        isCreate: false,
      }
    },
    methods: {
      init() {
        let _this = this;
        if (_this._value.userinfo == null) {
          _this.$router.push('/login')
        }
        if (_this.in_id !== 0) {
          _this.getInerfaces();
        } else {
          _this.isCreate = true;
        }
      },
      reset() {
        this.def = [];
        this.in_id = 0;
        this.isSelectForder = false;
        this.forderArr = [];
        this.responseStr = '';
        this.info = {in_url: 'http://', params: [], in_response_ok: '', in_response_err: '', in_name: '', fo_id: 0};
        this.params = [];
      },
      getForders() {
        let _this = this;
        _this._model.getRequest(_this._model.urls.foder_getFoders, {
          us_id: _this._value.userinfo.id, pr_id: _this._value.projectinfo.id
        }, (res, isErr) => {
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            if (res) {
              for (var i = 0; i < res.length; i++) {
                _this.forderArr.push({
                  label: res[i].name,
                  guesser: res[i].name,
                  id: res[i].id,
                });
              }
              _this.isSelectForder = true;
            }
          }
        })
      },
      onSelectForder() {
        let _this = this;
        _this.info.in_id = 0;
        _this.info.fo_id = _this.def[0].id;
        _this.onSave();
      },
      onSetOK() {
        let _this = this;
        _this.info.in_response_ok = _this.responseStr;
        _this.$message({
          title: '操作成功,记得保存',
          message: '_',
          type: 'success',
          closeable: true
        });
      },
      onSetErr() {
        let _this = this;
        _this.info.in_response_err = _this.responseStr;
        _this.$message({
          title: '操作成功,记得保存',
          message: '_',
          type: 'success',
          closeable: true
        });
      },
      onSave() {
        let _this = this;
        if (_this.isEmpty(_this.info.in_url) || _this.info.in_url.length < 10) {
          _this.$message({
            title: '参数异常',
            type: 'warning',
            message: '请正确填入要保存的url',
            closeable: true
          });
          return;
        }
        if (_this.isEmpty(_this.info.in_name)) {
          _this.$message({
            title: '参数异常',
            type: 'warning',
            message: '请正确填入要保存的接口名',
            closeable: true
          });
          return;
        }
        if (_this.info.fo_id == 0) {
          _this.forderArr = [];
          _this.getForders();
        } else {
          let instance = _this.$loading();
          _this.info.us_id = _this._value.userinfo.id;
          _this.info.params = JSON.stringify(_this.params);
          _this.info.in_doc = _this.$refs.md.getMarkdown();
          _this._model.getRequest(_this._model.urls.interface_saveInterface, _this.info, (res, isErr) => {
            instance.close();
            if (isErr) {
              _this.$notify({
                message: res,
                type: 'error'
              });
            } else {
              _this.$message({
                title: '操作成功',
                message: '_',
                type: 'success',
                closeable: true
              });
              if (_this.isCreate) {
                _this.reset();
              }
              _this.$emit('onChange', res);
            }
          })
        }
      },
      onSend() {
        let _this = this;
        if (_this.isEmpty(_this.info.in_url) || _this.info.in_url.length < 10) {
          _this.$message({
            title: '参数异常',
            type: 'warning',
            message: '请正确填入要请求的url',
            closeable: true
          })
        } else {
          let instance = _this.$loading();
          var req = {
            requestUrl: _this.info.in_url
          };
          $.each(_this.params, (index, item) => {
            var key = item.pa_key;
            var value = item.pa_value;
            if (!_this.isEmpty(key)) {
              req[key] = value;
            }
          });
          $.post(_this._model.urls.interface_getSendResponse, req, (res) => {
            instance.close();
            _this.responseStr = '' + res;
            _this.$message({
              title: '请求成功',
              message: '_',
              type: 'success',
              closeable: true
            });
          })
        }
      },
      resetJson(str) {
        try {
          if (typeof str == 'object') {
            return _this.syntaxHighlight(str);
          } else {
            str = ('' + str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            return _this.syntaxHighlight(JSON.parse(str));
          }
        } catch (e) {
          return str;
        }
      },
      getInerfaces() {
        let _this = this;
        let instance = _this.$loading();
        _this._model.getRequest(_this._model.urls.interface_getDetail, {
          us_id: _this._value.userinfo.id, in_id: _this.in_id
        }, (res, isErr) => {
          instance.close();
          if (isErr) {
            _this.$notify({
              message: res,
              type: 'error'
            });
          } else {
            _this.info = res;
            _this.params = res.params;
          }
        })
      },
      syntaxHighlight(txt) {
        let _this = this;
        if (_this.isEmpty(txt)) {
          return txt;
        }
        var json = txt;
        if (typeof json != 'string') {
          json = JSON.stringify(json, undefined, 1);
        }
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, (match) => {
          var cls = 'number';
          if (/^"/.test(match)) {
            if (/:$/.test(match)) {
              cls = 'key';
            } else {
              cls = 'string';
            }
          } else if (/true|false/.test(match)) {
            cls = 'boolean';
          } else if (/null/.test(match)) {
            cls = 'null';
          }
          return '<span class="' + cls + '">' + match + '</span>';
        });
      },
      copyToClipboard(str) {
        let _this = this;
        try {
          str = JSON.parse(str);
        } catch (e) {
          console.info(e);
        }
        // 创建元素用于复制
        var aux = document.createElement("input");
        // 获取复制内容
        var content = JSON.stringify(str);
        // 设置元素内容
        aux.setAttribute("value", content);
        // 将元素插入页面进行调用
        document.body.appendChild(aux);
        // 复制内容
        aux.select();
        // 将内容复制到剪贴板
        document.execCommand("copy");
        // 删除创建元素
        document.body.removeChild(aux);
        _this.$message({
          title: '复制成功',
          message: ' ',
          type: 'success',
          closeable: true
        });
      },
    },
    watch: {},
    props: ['in_id'],
    components: {
      'v-editor': editor
    },
    computed: {},
    mounted() {
      this.init();
    },
  }
</script>
<style>
  .am-selected {
    width: auto;
  }
</style>
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

  .para-layout input {
    font-family: "wenquanyi micro hei";
  }

  @media only screen and (min-width: 641px) {
    .cus-boder {
      border-right: none;
    }
  }

  @media only screen and (max-width: 640px) {
    .cus-boder-t {
      border-top: none;
    }
  }

  .inline-pad {
    padding: 1px 3px !important;
  }

  .am-breadcrumb > li + li:before {
    padding: 0;
  }

  .am-panel {
    margin: 0 10px 10px;
  }

  .am-breadcrumb {
    margin-left: 10px;
    margin-bottom: 0;
  }

  .btn-del {
    border-radius: 100px 0 0 100px;
  }
</style>

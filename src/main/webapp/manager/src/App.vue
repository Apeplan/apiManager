<template>
  <div id="app">
    <transition :name="tname">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </transition>
  </div>
</template>

<script>

  let _this = null;
  let hisPath = [];

  export default {
    name: 'App',
    data() {
      return {
        tname: 'none'
      }
    },
    methods: {
      init() {
        var info1 = _this._value.userinfo;
        var info2 = _this._cache.get('userinfo');
        if (info1 === null && !_this.isEmpty(info2)) {
          _this._value.userinfo = info2;
        }
      },
      created() {
        this.$nextTick(function () {
          this.init();
        })
      }
    },
    watch: {
      '$route'(to, from) {
        const toIndex = to.path;
        const fromIndex = from.path;
        _this.$nprogress.start({
          loading: true
        });
        if (_this.isExistByValue(hisPath, toIndex)) {
          to.meta.isBack = true;
          _this.removeByValue(hisPath, fromIndex);
        } else {
          hisPath.push(toIndex);
          to.meta.isBack = false;
        }
        if (fromIndex !== '/') {
          _this.tname = to.meta.isBack ? 'to-right' : 'to-left';
        }
      }
    },
    created() {
      this.$nextTick(() => {
        _this = this;
        _this.init();
      })
    },
  }
</script>

<style scoped>

  .to-left-enter-active, .to-left-leave-active, .to-right-enter-active, .to-right-leave-active {
    backface-visibility: hidden;
    will-change: transform;
    transition: all .5s;
    position: fixed;
    height: 100%;
    bottom: 0;
    top: 0;
  }

  /**
    待移入对象从设定值到无 white -> transparent
   */
  .to-left-enter {
    transform: translateX(100%);
  }

  /**
    待移入对象从无到设定值 transparent -> white
   */
  .to-left-enter-active {
  }

  /**
    待移出对象从设定值到无 white -> transparent
   */
  .to-left-leave {

  }

  /**
    待移出对象从无到设定值 transparent -> white
   */
  .to-left-leave-active {
    transform: translateX(-100%);
  }

  .to-right-enter {
    transform: translateX(-100%);
  }

  .to-right-enter-active {

  }

  .to-right-leave {
  }

  .to-right-leave-active {
    /*transform: translateX(100%);*/
  }


</style>

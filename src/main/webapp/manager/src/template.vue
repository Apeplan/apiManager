<template>
  <div v-cloak class="w100">

  </div>
</template>

<script>

  let _this = null;

  export default {
    data() {
      return {}
    },
    methods: {
      init() {
      },
      reset() {

      }
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

</style>

<template>
  <div class="pdfbodys" :style="{ height: screenHeight + 'px' }">
    <iframe :src="pdfSrc" height="100%" width="100%" frameborder="0"></iframe>
  </div>
</template>
<script>
export default {
  name: 'ShowPdf',
  props: {
    staffName: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      backendSrc: 'http://localhost:8085',
      screenWeight: 0, // 屏幕宽度
      screenHeight: 0 // 屏幕高度
    }
  },
  computed: {
    pdfSrc () {
      return this.backendSrc + '/pdfjs/web/viewer.html';
    }
  },
  mounted () {
    this.screenWeight = document.documentElement.clientWidth
    this.screenHeight = document.documentElement.clientHeight
  },

  methods: {}
}
</script>
<style scoped>
.pdfbodys {
  overflow: hidden !important;
}
</style>

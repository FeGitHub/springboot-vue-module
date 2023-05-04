<template>
  <div class="pdfbodys" :style="{ height: screenHeight + 'px' }">
    <iframe :src="src" width="100%" height="100%" frameborder="0"></iframe>
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
      src: 'http://localhost:8085/showPdf/pdfjs/web/viewer.html',
      screenWeight: 0, // 屏幕宽度
      screenHeight: 0 // 屏幕高度
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

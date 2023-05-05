<template>
  <div class="pdfbodys" :style="{ height: screenHeight + 'px' }">
    <span @click="sendMessage">测试点击</span>
    <iframe
      id="noCodepdfIframe"
      ref="noCodepdfIframe"
      :src="noCodePdfSrc"
      height="100%"
      width="100%"
      frameborder="0"
    ></iframe>
    <iframe
      id="codepdfIframe"
      ref="codepdfIframe"
      :src="codePdfSrc"
      height="100%"
      width="100%"
      frameborder="0"
    ></iframe>
  </div>
</template>
<script>
export default {
  name: 'ShowPdf',
  props: {
    backendSrc: {
      type: String,
      default: 'http://localhost:8085'
    },
    propCodePdfSrc: {
      type: String,
      default: ''
    },
    propNoCodePdfSrc: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      screenWeight: 0, // 屏幕宽度
      screenHeight: 0 // 屏幕高度
    }
  },
  computed: {
    noCodePdfSrc () {
      return (
        this.backendSrc +
        '/pdfjs/web/viewer.html?file=' +
        this.backendSrc +
        this.propNoCodePdfSrc
      )
    },
    codePdfSrc () {
      return (
        this.backendSrc +
        '/pdfjs/web/viewer.html?file=' +
        this.backendSrc +
        this.propCodePdfSrc
      )
    }
  },

  created () {},
  mounted () {
    this.screenWeight = document.documentElement.clientWidth
    this.screenHeight = document.documentElement.clientHeight
    window.addEventListener('message', this.handleMessage)
  },

  methods: {
    /***
     * 接收子组件的通信信息
     **/
    handleMessage (event) {
      let data = event.data
      console.log('子组件信息：', data)
      if (data.operatorType === 'print') {
        this.callCodepdfIframePrint()
      }
    },
    /***
     * 让子组件打印
     **/
    callCodepdfIframePrint () {
      let data = {
        operatorType: 'print'
      }
      this.sendMessage(data, 'codepdfIframe')
    },
    /***
     * 传递子组件通信信息
     **/
    sendMessage (data, ref) {
      this.$refs[ref].contentWindow.postMessage(data, '*')
    }
  }
}
</script>
<style scoped>
.pdfbodys {
  overflow: hidden !important;
}
</style>

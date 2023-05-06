<template>
  <div class="pdf-body" :style="{ height: screenHeight + 'px' }">
    <!--  <span @click="callCodepdfIframePrint">测试点击</span> -->
    <iframe
      v-if="showPdfSrcBiz"
      ref="showPdfIframe"
      id="showPdfIframe"
      :src="showPdfSrcFull"
      height="100%"
      width="100%"
      frameborder="0"
    ></iframe>
    <div class="print-pdf-iframe">
      <iframe
        v-if="printPdfSrcBiz"
        ref="printPdfIframe"
        id="printPdfIframe"
        :src="printPdfSrcFull"
        height="100%"
        width="100%"
        frameborder="0"
      ></iframe>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ShowPdf',
  props: {
    /***
     *  后端的基础请求资源地址
     **/
    backendSrc: {
      type: String,
      default: 'http://localhost:8085'
    },
    /***
     *  要打印的pdf的请求资源
     **/
    printPdfSrcBiz: {
      type: String,
      default: null
    },
    /***
     *  要预览展示的pdf的请求资源
     **/
    showPdfSrcBiz: {
      type: String,
      default: null
    },
    /***
     *   要附加的url请求参数
     *   overWritePrint 表示是否重写打印方法
     *   showLog 表示是否打印自定义扩展业务的日志
     **/
    overWriteUrl: {
      type: String,
      default: '&overWritePrint=true&showLog=true'
    }
  },
  data () {
    return {
      screenWeight: 0, // 屏幕宽度
      screenHeight: 0 // 屏幕高度
    }
  },
  computed: {
    /***
     *  展示的pdf
     **/
    showPdfSrcFull () {
      return (
        this.backendSrc +
        '/pdfjs/web/viewer.html?file=' +
        this.backendSrc +
        this.showPdfSrcBiz +
        (this.printPdfSrcBiz ? this.overWriteUrl : '')
      )
    },
    /***
     *  打印的pdf
     **/
    printPdfSrcFull () {
      return (
        this.backendSrc +
        '/pdfjs/web/viewer.html?file=' +
        this.backendSrc +
        this.printPdfSrcBiz +
        (this.printPdfSrcBiz ? this.overWriteUrl : '')
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
      console.log('来自子组件信息：', data)
      if (data.operatorType === 'print') {
        this.callPrintPdfIframePrint()
      }
    },
    /***
     *  对子组件发起打印动作
     **/
    callPrintPdfIframePrint () {
      let data = {
        operatorType: 'print'
      }
      this.sendMessage(data, 'printPdfIframe')
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
.pdf-body {
  overflow: hidden !important;
}
.print-pdf-iframe {
  visibility: hidden;
}
</style>

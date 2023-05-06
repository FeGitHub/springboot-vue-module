<template>
  <div>
    <div class="box" @click="clickA">AAA</div>
    <div class="pdf-box">
      <ShowPdf
        :print-pdf-src-biz="printPdfSrcBiz"
        :show-pdf-src-biz="showPdfSrcBiz"
      ></ShowPdf>
    </div>
    <div class="box">CCC</div>
  </div>
</template>
<script>
export default {
  mounted () {},
  data () {
    return {
      printPdfSrcBiz: '/test/createPdfByPdfTemplate',
      showPdfSrcBiz: '/test/createPdfByHtmlTemplate'
    }
  },
  created () {},
  methods: {
    clickA () {
      if (this.showPdfSrcBiz === '/test/createPdfByPdfTemplate') {
        this.showPdfSrcBiz = '/test/createPdfByHtmlTemplate';
      } else {
        this.showPdfSrcBiz = '/test/createPdfByPdfTemplate';
      }
    }
  }
}
</script>
<style>
.box {
  width: 13%;
  display: inline-block;
}

.pdf-box {
  width: 53%;
  display: inline-block;
}
</style>

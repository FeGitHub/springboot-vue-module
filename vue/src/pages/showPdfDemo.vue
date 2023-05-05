<template>
  <div>
    <div class="box" @click="clickA">AAA</div>
    <div class="pdf-box">
      <ShowPdf
        :prop-code-pdf-src="propCodePdfSrc"
        :prop-no-code-pdf-src="propNoCodePdfSrc"
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
      propCodePdfSrc: '/test/createPdfByPdfTemplate',
      propNoCodePdfSrc: '/test/createPdfByHtmlTemplate'
    }
  },
  created () {},
  methods: {
    clickA () {
      if (this.propNoCodePdfSrc === '/test/createPdfByPdfTemplate') {
        this.propNoCodePdfSrc = '/test/createPdfByHtmlTemplate';
      } else {
        this.propNoCodePdfSrc = '/test/createPdfByPdfTemplate';
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

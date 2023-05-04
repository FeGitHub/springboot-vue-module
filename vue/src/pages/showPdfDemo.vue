<template>
  <div>
    <div class="box">AAA</div>
    <!--<ShowPdf></ShowPdf>-->
    <div class="pdf-box">
      <ShowPdf></ShowPdf>
    </div>
    <div class="box">CCC</div>
  </div>
</template>
<script>
export default {
  mounted () {},
  data () {
    return {}
  },
  created () {},
  methods: {}
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

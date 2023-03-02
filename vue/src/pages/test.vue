<template>
  <div>
    <SubmitElDialog
      :staffName="staffName"
      :staffCode="staffCode"
      :showDialog="dialogVisible"
      @handleClose="handleClose"
    ></SubmitElDialog>
  </div>
</template>
<script>
import { testDownLoadTemplate } from '@/api/test/testApi';
import utils from '@/utils';
export default {
  mounted () {},
  data () {
    return {
      staffName: '牛頂天',
      staffCode: 'XXXXXXXXXX',
      dialogVisible: true
    }
  },
  created () {},
  methods: {
    handleClose () {
      this.dialogVisible = false
      this.testDownload()
    },
    testDownload () {
      testDownLoadTemplate({}).then(res => {
        utils.downloadExcel(res.data, '測試下載excel')
      })
    }
  }
}
</script>
<style>
.success {
  position: relative;
  left: -160px;
  top: -30px;
}
.el-icon-success {
  font-size: 56px;
  color: #3ab5c2;
}
.title-row span {
  font-size: 27px;
  position: relative;
  top: -10px;
  left: 10px;
}

.text-row {
  text-align: left;
  position: relative;
  left: 200px;
}

.btn-row {
  margin-right: -700px;
  font-size: 20px;
  margin-bottom: -10px;
}
.btn-row span {
  border: 1px solid green;
  padding: 15px 20px;
  border-radius: 10px;
  color: green;
  cursor: pointer;
}

.success p {
  margin-bottom: 20px;
}
.text-row {
  font-size: 18px;
  color: #808080a3;
}
</style>

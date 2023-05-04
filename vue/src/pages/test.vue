<template>
  <div>
    <ShowPdf></ShowPdf>
    <!--  <SubmitElDialog
      :staffName="staffName"
      :staffCode="staffCode"
      :showDialog="dialogVisible"
      @handleClose="handleClose"
    ></SubmitElDialog>
  <el-select
      v-model="value"
      filterable
      placeholder="请选择"
      :filter-method="filterQuery"
    >
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select> -->
  </div>
</template>
<script>
// import { testDownLoadTemplate, createPdfByTemplate } from '@/api/test/testApi';
import { createPdfByTemplate } from '@/api/test/testApi';
import utils from '@/utils';
export default {
  mounted () {},
  data () {
    return {
      staffName: '牛頂天',
      staffCode: 'XXXXXXXXXX',
      dialogVisible: true,
      options: [
        {
          // 用于显示的数据
          value: '选项1',
          label: '黄金糕'
        },
        {
          value: '选项2',
          label: '双皮奶'
        },
        {
          value: '选项3',
          label: '蚵仔煎'
        },
        {
          value: '选项4',
          label: '龙须面'
        },
        {
          value: '选项5',
          label: '北京烤鸭'
        }
      ],
      value: '', // select 选中值
      oldOptions: [] // 保存后端原始数据
    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      this.oldOptions = JSON.parse(JSON.stringify(this.options))
    },
    filterQuery (value) {
      if (value) {
        this.options = this.oldOptions.filter(
          item =>
            item.label.indexOf(value) > -1 || item.value.indexOf(value) > -1
        )
      } else {
        this.options = this.oldOptions
      }
    },
    handleClose () {
      this.dialogVisible = false
      this.testDownload()
    },
    testDownload () {
      /* testDownLoadTemplate({}).then(res => {
        utils.downloadExcel(res.data, '測試下載excel')
      }) */
      createPdfByTemplate({}).then(res => {
        utils.download(res.data, '測試下載pdf.pdf')
      })
    }
  }
}
</script>
<style>
html,
body {
  height: 100%;
  padding: 0;
  margin: 0;
}
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

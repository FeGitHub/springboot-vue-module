<template>
  <div>
    <el-dialog
      class="previewDialog"
      width="30%"
      :visible.sync="showDialog"
      :show-close="false"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="success">
        <p class="title-row">
          <i class="el-icon-success"></i>
          <span>{{ tip }}</span>
        </p>
        <p class="text-row">
          <span>{{ showText }}</span>
        </p>
        <p class="btn-row">
          <span @click="handleClose">{{ tipMsg }}</span>
        </p>
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'SubmitElDialog',
  props: {
    staffName: {
      type: String,
      default: '-'
    },
    staffCode: {
      type: String,
      default: '-'
    },
    showDialog: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      tip: '提示',
      tipMsg: '知道了'
    }
  },
  computed: {
    showText () {
      return this.staffName + ' ' + this.staffCode
    }
  },
  mounted () {},

  methods: {
    handleClose () {
      this.$emit('handleClose')
    }
  }
}
</script>
<style scoped>
.success {
  position: relative;
  top: -6px;
}
.el-icon-success {
  font-size: 45px;
  color: rgb(62, 180, 166);
  font-weight: bold;
}
.title-row span {
  font-size: 25px;
  position: relative;
  top: -10px;
  left: 10px;
}

.text-row {
  font-size: 18px;
  color: #808080a3;
  padding: 0px 55px;
}

.btn-row span {
  border: 1px solid rgb(82, 187, 175);
  padding: 10px 20px;
  border-radius: 10px;
  color: rgb(82, 187, 175);
  cursor: pointer;
  margin-right: 30px;
  font-weight: bold;
  font-size: 18px;
}
</style>

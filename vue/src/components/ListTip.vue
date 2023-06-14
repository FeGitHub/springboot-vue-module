<template>
  <div style="display: inline">
    顯示數據
    <el-popover
      v-if="warningContent.length > 0 || errorContent.length > 0"
      placement="right-start"
      trigger="hover"
      popper-class="profileMenuTipPopover"
      :visible-arrow="false"
      @show="tipStatusChange(true)"
      @hide="tipStatusChange(false)"
    >
      <div class="tipBox">
        <div v-if="errorContent.length > 0" class="errorTip">
          <template v-for="(item, index) in errorContent">
            <span :key="item">{{ index + 1 }}、{{ item.title }} </span>
            <div :key="item">
              <span class="content-class">{{ item.content }}</span>
            </div>
          </template>
        </div>
        <div v-if="warningContent.length > 0" class="warningTip">
          <template v-for="(item, index) in warningContent">
            <span :key="item">{{ index + 1 }}、{{ item.title }} </span>
            <div :key="item">
              <span class="content-class">{{ item.content }}</span>
            </div>
          </template>
        </div>
      </div>
      <i
        slot="reference"
        class="el-icon-warning iconClass"
        :style="{ color: errorContent.length > 0 ? 'red' : '#FFCF52' }"
      />
    </el-popover>
  </div>
</template>

<script>
export default {
  name: 'ListTip',
  props: {
    rowData: {
      type: Object,
      default: null
    }
  },

  data () {
    return {}
  },
  computed: {
    errorContent () {
      return [
        {
          title: '【重點標題】',
          content: '錯誤的提示2222222222222222222111111111111111111111111111'
        },
        {
          title: '【重點標題】',
          content: '錯誤的提示2222222222222222222111111111111111111111111111'
        }
      ]
    },
    warningContent () {
      return [
        {
          title: '【重點標題】',
          content: '錯誤的提示2222222222222222222111111111111111111111111111'
        },
        {
          title: '【重點標題】',
          content: '錯誤的提示2222222222222222222111111111111111111111111111'
        }
      ]
    }
  },
  created () {
    console.log(this.rowData)
  },

  methods: {}
}
</script>

<style>
.content-class {
  margin-left: 28px;
}
.tipBox {
  font-family: "Microsoft YaHei", 微软雅黑, "Microsoft JhengHei", 华文细黑,
    STHeiti, MingLiu, ui-rounded; /* 字體暫定 */
  font-weight: bold;
  font-style: normal;
  font-size: 14px;
  text-align: left;
  line-height: normal;
}

.warningTip {
  padding: 8px;
  color: rgb(255, 185, 0);
  background-color: rgb(255, 250, 238);
}

.errorTip {
  padding: 8px;
  color: #ff5050;
  background-color: rgb(255, 238, 238);
}

.iconClass {
  padding-bottom: 4px;
}

.profileMenuTipPopover {
  padding: 0 !important;
}
</style>

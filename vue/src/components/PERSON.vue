<template>
  <div>
    <div style="margin-bottom:20px;">
      <Input placeholder="身份证号码"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.IDENTITY_NUMBER" />
      <Input placeholder="姓名"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.PERSON_NAME" />
      <i-select style="width:100px;margin-right:10px;"
                v-model="queryForm.GENDER">
        <i-option v-for="item in dicts.GENDER"
                  :value="item.code"
                  v-bind:key="item.code">{{ item.name }}</i-option>
      </i-select>
      <Input placeholder="出生年份"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.BIRTH_YEAR" />
      <Button type="primary"
              @click="research()"
              style="margin-right:10px;">查询</Button>
      <Button type="primary"
              @click="add()">新建</Button>
    </div>
    <Table border
           ref="selection"
           :columns="columns12"
           :data="data6">
      <template slot-scope="{ row }"
                slot="PERSON_NAME">
        <strong>{{ row.PERSON_NAME }}</strong>
      </template>
      <template slot-scope="{ row }"
                slot="action">
        <Button type="primary"
                size="small"
                style="margin-right: 5px"
                @click="edit(row.ID)">修改</Button>
        <Button type="error"
                size="small"
                @click="remove(row.ID)"
                style="margin-right: 5px">删除</Button>
        <Button type="primary"
                size="small"
                @click="show(row.ID)">查看</Button>
      </template>
    </Table>
    <div style="margin-top:20px;">
      <Page :total=4
            show-sizer
            show-elevator></Page>
    </div>
  </div>
</template>
<script>
import https from "../https";
export default {
  mounted () {
    let param = {};
    https
      .Post("/api/person/list", param)
      .then(res => {
        console.log("-----------");
        console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  },
  data () {
    return {
      queryForm: {
        IDENTITY_NUMBER: "",
        PERSON_NAME: "",
        GENDER: "",
        BIRTH_YEAR: ""
      },
      dicts: [],//字典
      columns12: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '姓名',
          slot: 'PERSON_NAME'
        },
        {
          title: '身份证号码',
          key: 'IDENTITY_NUMBER'
        },
        {
          title: '性别',
          key: 'GENDER'
        },
        {
          title: '出生日期',
          key: 'BIRTH_TIME'
        },
        {
          title: '配偶姓名',
          key: 'SPOUSE_NAME'
        },
        {
          title: '操作',
          slot: 'action',
          width: 250,
          align: 'center'
        }
      ],
      data6: [
        {
          PERSON_NAME: 'John Brown',
          IDENTITY_NUMBER: 18,
          GENDER: 'New York No. 1 Lake Park',
          BIRTH_TIME: 'BIRTH_TIME',
          SPOUSE_NAME: 'SPOUSE_NAME',
          ID: '1'
        },
        {
          PERSON_NAME: 'Jim Green',
          IDENTITY_NUMBER: 24,
          GENDER: 'London No. 1 Lake Park',
          BIRTH_TIME: 'BIRTH_TIME',
          SPOUSE_NAME: 'SPOUSE_NAME',
          ID: '2'
        }

      ]
    }
  },
  methods: {

    research () {
      console.log(this.queryForm);
      if (!this.checkBeforeResearch()) {
        return;
      }
    },

    checkBeforeResearch () {
      //3、出生年份，文本录入，只能录入数字，比如2015；
      //查询为精确查找
      let params = this.queryForm;
      let times = 0;
      //1、若不输入“身份证号码”，“姓名、性别、出生年份”查询条件至少要填写其中两项才能进行查询，否则提示：至少输入两个条件。
      if (!params.IDENTITY_NUMBER) {
        for (let key in params) {
          if (params[key]) {
            times++;
          }
        }
      }
      if (times < 0) {
        alert("至少输入两个条件");
        return false;
      }
      return true;

    },
    add () {
    },
    show (index) {
      alert(index);
    },
    remove (index) {

    },
    edit (index) {
      alert(index);
    },
    selectAll (status) {
      this.$refs.selection.selectAll(status);
      console.log(this.$refs.selection);
    }

  }
}
</script>



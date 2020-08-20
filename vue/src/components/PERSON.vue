<template>
  <div>

    <Modal title="信息维护"
           v-model="modal"
           :maskClosable="false"
           :loading="modalLoading"
           @on-ok="ok"
           ref="modal">
      <div slot="header"
           style="fontSize;fontWeight:bold">
        <span>{{titleContent}}</span>
      </div>
      <Form :label-width="120"
            :disabled="editDisabled"
            :rules="formRules"
            ref="ModalForm"
            :model="ModalForm">
        <FormItem label="姓名"
                  prop="personName">
          <i-input placeholder="请输入..."
                   style="width: 220px"
                   v-model="ModalForm.personName"></i-input>
        </FormItem>

        <FormItem label="身份证号码"
                  prop="identityNumber">
          <i-input placeholder="请输入..."
                   style="width: 220px"
                   v-model="ModalForm.identityNumber"></i-input>
        </FormItem>
        <FormItem label="性别"
                  prop="gender">
          <i-select placeholder="性别"
                    style="width:220px;margin-right:10px;"
                    v-model="ModalForm.gender">
            <i-option v-for="item in dicts.GENDER"
                      :value="item.CODE"
                      v-bind:key="item.CODE">{{ item.NAME }}</i-option>
          </i-select>
        </FormItem>
        <FormItem label="出生日期"
                  prop="birthTime">

          <Date-picker placeholder="选择日期"
                       style="width: 220px"
                       @on-change="dateChange"
                       type="datetime"
                       format="yyyy-MM-dd"
                       v-model="ModalForm.birthTime"></Date-picker>

        </FormItem>

        <FormItem label="婚姻情况"
                  prop="maritalStatus">
          <i-select placeholder="婚姻情况"
                    style="width:220px;margin-right:10px;"
                    v-model="ModalForm.maritalStatus">
            <i-option v-for="item in dicts.MARITAL_STATUS"
                      :value="item.CODE"
                      v-bind:key="item.CODE">{{ item.NAME }}</i-option>
          </i-select>
        </FormItem>
        <FormItem label="配偶姓名"
                  prop="spouseName">
          <i-input placeholder="请输入..."
                   style="width: 220px"
                   v-model="ModalForm.spouseName"></i-input>
        </FormItem>

      </Form>
      <div slot="footer">
        <div>
          <Button size="large"
                  style="margin-left:8px"
                  @click="close()">关闭</Button>
          <Button type="primary"
                  v-if="titleContent!='人员信息查看'"
                  size="large"
                  style="margin-left:8px"
                  @click="savePerson('ModalForm')">保存</Button> </div>

      </div>
    </Modal>

    <div style="margin-bottom:20px;">
      <Input placeholder="身份证号码"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.identityNumber" />
      <Input placeholder="姓名"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.personName" />
      <RadioGroup v-model="queryForm.gender"
                  @on-change="changeGender">
        <Radio label="1">男</Radio>
        <Radio label="2">女</Radio>
      </RadioGroup>
      <Input placeholder="出生年份"
             style="width: 200px;margin-right:10px;"
             v-model="queryForm.birthYear"
             :maxlength="4" />
      <Button type="primary"
              @click="research('check')"
              style="margin-right:10px;">查询</Button>
      <Button type="primary"
              @click="addPerson()">新建</Button>

    </div>
    <Table border
           ref="selection"
           :columns="columns"
           :data="pageData">
      <template slot-scope="{ row }"
                slot="PERSON_NAME">
        <strong>{{ row.PERSON_NAME }}</strong>
      </template>
      <template slot-scope="{ row }"
                slot="action">
        <Button type="primary"
                size="small"
                style="margin-right: 5px"
                @click="editPerson(row.ID)">修改</Button>
        <Button type="error"
                size="small"
                @click="deletePerson(row.ID)"
                style="margin-right: 5px">删除</Button>
        <Button type="primary"
                size="small"
                @click="viewPerson(row.ID)">查看</Button>
      </template>
    </Table>
    <div style="margin-top:20px;">
      <Page :total="page.total"
            show-sizer
            @on-change="onPagechange"
            @on-page-size-change="onPageSizeChange"></Page>
    </div>
  </div>
</template>
<script>
import https from "../https";
import utils from "../utils";
import dayjs from "dayjs";
export default {
  mounted () {
    let param = { "dicts": "MARITAL_STATUS,GENDER" };
    https
      .Post("/comm/getDicts", param)
      .then(res => {
        let data = res.data;
        this.dicts = data.data;

      })
      .catch(err => {
        console.log(err);
      });
  },
  data () {
    return {
      formRules: {
        personName: [
          { required: true, message: "姓名不能为空！", trigger: 'change' },
        ],
        identityNumber: [
          { required: true, message: "身份证号码不能为空！", trigger: 'change' },
        ],
        gender: [
          { required: true, message: "性别不能为空！", trigger: 'change' },
        ],
        birthTime: [
          { required: true, message: "出生日期不能为空！", trigger: 'change', pattern: /.+/ },
        ],
        maritalStatus: [
          { required: true, message: "婚姻状况不能为空！", trigger: 'change' },
        ]
      },
      ModalForm: {
        birthTime: "",
        identityNumber: "",
        maritalStatus: "",
        spouseName: "",
        personName: "",
        gender: ""
      },
      clearModalForm: {
        birthTime: "",
        identityNumber: "",
        maritalStatus: "",
        spouseName: "",
        personName: "",
        gender: ""
      },
      page: {
        curPageNum: 1,
        pageSize: 10,
        total: 0
      },
      editDisabled: false,//弹框是否可编辑
      modalLoading: true,
      modal: false,//模态框的展示
      titleContent: "",//弹出框标题
      queryForm: {
        IDENTITY_NUMBER: "",
        PERSON_NAME: "",
        GENDER: "",
        BIRTH_YEAR: ""
      },
      dicts: {
        MARITAL_STATUS: [],
        GENDER: []
      },
      columns: [

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
          key: 'GENDERNAME'
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
      pageData: [


      ]
    }
  },
  methods: {

    changeGender (value) {
      console.log(value);
    },
    dateChange (date) { // 检测日期组件的改变
      this.ModalForm.birthTime = date;
    },
    /***
     * 关闭弹出框
     */
    close () {
      this.ModalForm = { ...this.clearModalForm };
      this.$refs.modal.visible = false;
      this.titleContent = "";
    },
    onPagechange (curPageNum) {
      this.page.curPageNum = curPageNum;
      this.research("");
    },
    onPageSizeChange (pageSize) {
      console.log(pageSize);
      this.page.pageSize = pageSize;
      this.research("");
    },
    open () {
      this.ModalForm = { ...this.clearModalForm };
      this.$refs.modal.visible = true;
      this.modalLoading = false;
    },
    ok () {
      this.$refs.modal.visible = true;
      this.modalLoading = false;
      this.open();
      return

    },
    /**
     * 查询
     */
    research (flag) {
      if (flag == 'check' && !this.checkBeforeResearch()) {
        return;
      }
      let params = {
        ...this.queryForm,
        ...this.page
      };
      https
        .Post("/person/queryList", params)
        .then(res => {
          let data = res.data;
          if (data.code == 200) {
            this.pageData = data.data.dataset;
            this.page.total = data.data.total;
          } else {
            this.$Message.error(res.data.message);
          }
        })
        .catch(err => {
          console.log(err);
          this.close();
        });
    },

    checkBeforeResearch () {
      //3、出生年份，文本录入，只能录入数字，比如2015；
      //查询为精确查找
      let params = this.queryForm;
      let times = 0;
      //1、若不输入“身份证号码”，“姓名、性别、出生年份”查询条件至少要填写其中两项才能进行查询，否则提示：至少输入两个条件。
      if (!params.identityNumber) {
        for (let key in params) {
          if (params[key]) {
            times++;
          }
        }
        if (times < 2) {
          this.$Message.error('至少输入两个条件', 4);
          return false;
        }
      }

      return true;

    },
    /**
     * 删除人员信息(目前是物理删除)
     */
    deletePerson (id) {
      this.$Modal.confirm({
        title: '提示信息',
        content: '是否删除?',
        onOk: () => {
          https
            .Post("/person/delete", { "id": id })
            .then(res => {
              let data = res.data;
              if (data.code == 200) {
                this.$Message.error('删除成功', 4);
                this.research("");
              } else {
                this.$Message.error(res.data.message);
              }
            })
            .catch(err => {
              console.log(err);

            });
        }
      });
    },


    addPerson () {
      this.titleContent = "新增人员信息";
      this.editDisabled = false;
      this.open();
    },
    editPerson (id) {
      this.titleContent = "人员信息维护";
      this.editDisabled = false;
      this.handlePersoninfo(id);
    },
    viewPerson (id) {
      this.titleContent = "人员信息查看";
      this.editDisabled = true;
      this.handlePersoninfo(id);
    },
    /**
     * 处理单个的人员信息
     */
    handlePersoninfo (id) {
      this.open();
      https
        .Post("/person/detail", { "id": id })
        .then(res => {
          let data = res.data;
          if (data.code == 200) {
            this.ModalForm = {
              ...data.data
            };
          } else {
            this.$Message.error(res.data.message);
          }
        })
        .catch(err => {
          console.log(err);
          this.close();
        });
    },
    /***
     * 保存前校验
     */
    checkBeforeSave () {
      if (!utils.isCardId(this.ModalForm.identityNumber)) {
        this.$Message.error('不合法的身份证号码!');
        return false;
      }
      if (this.ModalForm.birthTime != utils.getBirthday(this.ModalForm.identityNumber)) {
        console.log(utils.getBirthday(this.ModalForm.identityNumber));
        this.$Message.error('出生日期与身份证号码不匹配!');
        return false;
      }
      //出生日期不能晚于当前日期；校验提示：出生日期不能晚于当前日期
      if (dayjs(this.ModalForm.birthTime).isAfter(dayjs())) {
        this.$Message.error('出生日期不能晚于当前日期!');
        return false;
      }
      return true;
    },
    /**
     * 保存人员
     */
    savePerson (name) {
      let url = !this.ModalForm.ID ? "/person/add" : "/person/update";
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (!this.checkBeforeSave()) {
            return;
          }
          //身份证号码长度为18位，尾数如果输入小写x，保存时，自动转换成大写X(直接转换成大写)
          this.ModalForm.identityNumber = this.ModalForm.identityNumber.toUpperCase();
          https
            .Post(url, this.ModalForm)
            .then(res => {
              let data = res.data;
              if (data.code == 200) {
                this.$Message.success('保存成功!');
                this.close();
                this.research("");
              } else {
                this.$Message.error(data.message);
              }
            })
            .catch(err => {
              console.log(err);
              this.close();
            });
        } else {
          this.$Message.error('表单验证失败!');
        }
      })
    },
  }
}
</script>



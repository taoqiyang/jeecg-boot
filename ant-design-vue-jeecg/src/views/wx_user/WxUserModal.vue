<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="openId" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'openId', validatorRules.openId]" placeholder="请输入openId" readOnly></a-input>
        </a-form-item>
        <a-form-item label="呢称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'nickName', validatorRules.nickName]" placeholder="请输入呢称" readOnly></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'gender', {}]" placeholder="请选择性别" disabled>
            <a-select-option :value="0">女</a-select-option>
            <a-select-option :value="1">男</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'city', validatorRules.city]" placeholder="请输入城市" readOnly></a-input>
        </a-form-item>
        <a-form-item label="省份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'province', validatorRules.province]" placeholder="请输入省份" readOnly></a-input>
        </a-form-item>
        <a-form-item label="国家" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'country', validatorRules.country]" placeholder="请输入国家" readOnly></a-input>
        </a-form-item>
        <a-form-item label="头像地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'avatarUrl', validatorRules.avatarUrl]" placeholder="请输入头像地址" readOnly></a-input>
        </a-form-item>
        <a-form-item label="微信统一用户标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'unionId', validatorRules.unionId]" placeholder="未获取" readOnly></a-input>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mobile', validatorRules.mobile]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <a-form-item label="会员级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'vipLevel', {}]" placeholder="请选择会员级别">
            <a-select-option :value="0">非会员</a-select-option>
            <a-select-option :value="1">会员</a-select-option>
          </a-select>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  
  export default {
    name: "WxUserModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules:{
        openId:{rules: [{ required: true, message: '请输入openId!' }]},
        nickName:{rules: [{ required: true, message: '请输入呢称!' }]},
        gender:{},
        city:{},
        province:{},
        country:{},
        avatarUrl:{},
        unionId:{},
        mobile:{},
        vipLevel:{rules: [{ required: true, message: '请输入会员级别!' }]},
        },
        url: {
          add: "/admin/wx/user/add",
          edit: "/admin/wx/user/edit",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'openId','nickName','gender','city','province','country','avatarUrl','unionId','mobile','vipLevel'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'openId','nickName','gender','city','province','country','avatarUrl','unionId','mobile','vipLevel'))
      }
      
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>
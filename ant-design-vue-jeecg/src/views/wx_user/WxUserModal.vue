<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="openId" :labelCol="labelCol" :wrapperCol="wrapperCol" disabled>
          <a-input v-decorator="[ 'openid', validatorRules.openid]" placeholder="请输入openId"></a-input>
        </a-form-item>
        <a-form-item label="呢称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'nickname', validatorRules.nickname]" placeholder="请输入呢称"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'gender', validatorRules.gender]" placeholder="请输入性别" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'city', validatorRules.city]" placeholder="请输入城市"></a-input>
        </a-form-item>
        <a-form-item label="省份" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'province', validatorRules.province]" placeholder="请输入省份"></a-input>
        </a-form-item>
        <a-form-item label="国家" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'country', validatorRules.country]" placeholder="请输入国家"></a-input>
        </a-form-item>
        <a-form-item label="头像地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'avatarurl', validatorRules.avatarurl]" placeholder="请输入头像地址"></a-input>
        </a-form-item>
        <a-form-item label="微信统一用户标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'unionid', validatorRules.unionid]" placeholder="请输入微信统一用户标识"></a-input>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mobile', validatorRules.mobile]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <a-form-item label="会员级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'vipLevel', validatorRules.vipLevel]" placeholder="请输入会员级别" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
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
        openid:{rules: [{ required: true, message: '请输入openId!' }]},
        nickname:{rules: [{ required: true, message: '请输入呢称!' }]},
        gender:{},
        city:{},
        province:{},
        country:{},
        avatarurl:{},
        unionid:{},
        mobile:{},
        vipLevel:{rules: [{ required: true, message: '请输入会员级别!' }]},
        },
        url: {
          add: "/./wxUser/add",
          edit: "/./wxUser/edit",
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
          this.form.setFieldsValue(pick(this.model,'openid','nickname','gender','city','province','country','avatarurl','unionid','mobile','vipLevel'))
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
        this.form.setFieldsValue(pick(row,'openid','nickname','gender','city','province','country','avatarurl','unionid','mobile','vipLevel'))
      },

      
    }
  }
</script>
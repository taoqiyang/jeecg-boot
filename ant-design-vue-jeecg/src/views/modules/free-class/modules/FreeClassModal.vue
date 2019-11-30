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

        <a-form-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'title', validatorRules.title]" placeholder="请输入标题"></a-input>
        </a-form-item>
        <a-form-item label="封面图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['cover']" :trigger-change="true"></j-upload>
        </a-form-item>
        <a-form-item label="视频地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'videoUrl', validatorRules.videoUrl]" addonBefore="https://" placeholder="请输入视频地址"></a-input>
        </a-form-item>
        <a-form-item label="发布状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-select v-decorator="[ 'sendStatus', {}]" placeholder="请选择发布状态">
            <a-select-option :value="0">不发布</a-select-option>
            <a-select-option :value="1">发布</a-select-option>
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
  import JUpload from '@/components/jeecg/JUpload'
  
  export default {
    name: "FreeClassModal",
    components: { 
      JUpload,
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
        title:{rules: [{ required: true, message: '请输入标题!' }]},
        cover:{rules: [{ required: true, message: '请上传一张封面图片!' }]},
        videoUrl:{rules: [{ required: true, message: '请输入视频地址!' }]},
        sendStatus:{rules: [{ required: true, message: '请选择发布状态!' }]},
        },
        url: {
          add: "/free_class/freeClass/add",
          edit: "/free_class/freeClass/edit",
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
        if(this.model) {
          this.model.videoUrl = this.model.videoUrl.substring(8)
        }
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'title','cover','videoUrl','sendStatus'))
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
            if(values.cover.indexOf(',') >= 0){
              that.$message.warning("只能上传一张封面图片");
              return;
            }
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
            formData.videoUrl = "https://" + formData.videoUrl
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
        this.form.setFieldsValue(pick(row,'title','cover','videoUrl','sendStatus'))
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
<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-form-item label="课程标题" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
          <a-input v-decorator="[ 'title', validatorRules.title]" placeholder="请输入课程标题"></a-input>
        </a-form-item>
        <a-form-item label="课程描述" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
          <a-textarea v-decorator="['description']" rows="4" placeholder="请输入课程描述"/>
        </a-form-item>
        <a-form-item label="封面图片" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
          <j-upload v-decorator="['cover']" :trigger-change="true"></j-upload>
          <img v-if="form.cover" :src="form.cover"/>
        </a-form-item>
        <a-form-item label="课程标签" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
          <a-select
            mode="tags"
            style="width: 100%"
            :tokenSeparators="[',']"
            v-decorator="[ 'tagList', validatorRules.tags]"
            placeholder="输入小学、中学等关键字按回车生成标签。"
          ></a-select>
          <!-- <a-input v-decorator="[ 'tags', validatorRules.tags]" placeholder="请输入课程标签"></a-input> -->
        </a-form-item>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="课程课时表" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="courseLessonTable.loading"
            :columns="courseLessonTable.columns"
            :dataSource="courseLessonTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"
          />
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </a-modal>
</template>

<script>

import pick from 'lodash.pick'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import JUpload from '@/components/jeecg/JUpload'

export default {
  name: 'CourseModal',
  mixins: [JEditableTableMixin],
  components: {
    JUpload,
  },
  data () {
    return {
      labelCol2: {
        span: 3
      },
      wrapperCol2: {
        span: 20
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {
        title: { rules: [{ required: true, message: '请输入课程标题!' }] },
        description: { rules: [{ required: true, message: '请输入课程描述!' }] },
        cover: { rules: [{ required: true, message: '请输入封面图片!' }] },
        tags: { rules: [{ required: true, message: '请输入课程标签!' }] },
      },
      refKeys: ['courseLesson',],
      tableKeys: ['courseLesson',],
      activeKey: 'courseLesson',
      // 课程课时表
      courseLessonTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '课时标题',
            key: 'title',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
          },
          {
            title: '封面图片',
            key: 'cover',
            type: FormTypes.image,
            token: true,
            responseName: "message",
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
          },
          {
            title: '视频地址',
            key: 'videoUrl',
            type: FormTypes.input,
            width: "200px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
          },
        ]
      },
      url: {
        add: "/course/course/add",
        edit: "/course/course/edit",
        courseLesson: {
          list: '/course/course/queryCourseLessonByMainId'
        },
      }
    }
  },
  methods: {
    getAllTable () {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter () {
      let fieldval = pick(this.model, 'title', 'description', 'cover', 'tagList')
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval)
      })
      // 加载子表数据
      if (this.model.id) {
        let params = { id: this.model.id }
        this.requestSubTableData(this.url.courseLesson.list, params, this.courseLessonTable)
      }
    },
    /** 整理成formData */
    classifyIntoFormData (allValues) {
      let main = Object.assign(this.model, allValues.formValue)

      return {
        ...main, // 展开
        courseLessonList: allValues.tablesValue[0].values,
      }
    },
    validateError (msg) {
      this.$message.error(msg)
    },
    popupCallback (row) {
      this.form.setFieldsValue(pick(row, 'title', 'description', 'cover', 'tagList', 'createTime', 'sendStatus', 'delFlag'))
    },

  }
}
</script>

<style scoped>
</style>
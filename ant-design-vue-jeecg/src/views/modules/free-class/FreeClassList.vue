<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="标题">
              <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="发布状态">
              <a-select v-model="queryParam.sendStatus" placeholder="请选择发布状态">
                <a-select-option :value="0">未发布</a-select-option>
                <a-select-option :value="1">已发布</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
              >重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('免费试听课程')">导出</a-button> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>-->

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <!-- :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}" -->

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt="图片不存在"
            style="max-width:80px;font-size: 12px;font-style: italic;"
          >
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)"
          >下载</a-button>
        </template>

        <span slot="sendStatus" slot-scope="sendStatus,record">
          <a-tag :color="sendStatus === 0 ? 'volcano' : 'green'">{{record.sendStatus_dictText}}</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a-menu-item>
            <a-popconfirm
              :title="`确定${record.sendStatus === 0 ? '发布' : '取消发布'}吗?`"
              @confirm="() => handleSend(record)"
            >
              <a>{{record.sendStatus === 0 ? '发布' : '取消发布'}}</a>
            </a-popconfirm>
          </a-menu-item>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多
              <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item @click="handleEdit(record)">
                <a>编辑</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <freeClass-modal ref="modalForm" @ok="modalFormOk"></freeClass-modal>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import FreeClassModal from './modules/FreeClassModal'
import { httpAction } from '@/api/manage'

export default {
  name: "FreeClassList",
  mixins: [JeecgListMixin],
  components: {
    FreeClassModal
  },
  data () {
    return {
      description: '免费试听课程管理页面',
      // 表头
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key: 'rowIndex',
        //   width: 60,
        //   align: "center",
        //   customRender: function (t, r, index) {
        //     return parseInt(index) + 1;
        //   }
        // },
        {
          title: '标题',
          align: "center",
          dataIndex: 'title'
        },
        {
          title: '封面图片',
          align: "center",
          dataIndex: 'cover',
          scopedSlots: { customRender: 'imgSlot' }
        },
        {
          title: '视频地址',
          align: "center",
          dataIndex: 'videoUrl'
        },
        {
          title: '发布状态',
          align: "center",
          dataIndex: 'sendStatus',
          scopedSlots: { customRender: 'sendStatus' },
          width: 60
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: "/free_class/freeClass/list",
        delete: "/free_class/freeClass/delete",
        deleteBatch: "/free_class/freeClass/deleteBatch",
        exportXlsUrl: "/free_class/freeClass/exportXls",
        importExcelUrl: "free_class/freeClass/importExcel",
        edit: "/free_class/freeClass/edit",
      },
    }
  },
  computed: {
    
  },
  methods: {
    handleSend (record) {
      let formData = {
        id: record.id,
        sendStatus: record.sendStatus === 0 ? 1 : 0
      }
      var that = this;
      httpAction(this.url.edit, formData, 'put').then((res) => {
        if (res.success) {
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      }).catch(e => {
        console.error(e)
      })
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
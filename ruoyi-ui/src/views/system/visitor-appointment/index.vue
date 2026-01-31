<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="访客姓名" prop="visitorName">
        <el-input
          v-model="queryParams.visitorName"
          placeholder="请输入访客姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约部门" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入预约部门ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择预约状态" clearable>
          <el-option label="待确认" value="0" />
          <el-option label="已确认" value="1" />
          <el-option label="已拒绝" value="2" />
          <el-option label="已完成" value="3" />
          <el-option label="已取消" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="日期范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:visitor-appointment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-check"
          size="mini"
          @click="handleConfirm"
          v-hasPermi="['system:visitor-appointment:confirm']"
        >确认</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-close"
          size="mini"
          @click="handleReject"
          v-hasPermi="['system:visitor-appointment:reject']"
        >拒绝</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appointmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预约ID" prop="appointmentId" align="center" />
      <el-table-column label="访客姓名" prop="visitorName" align="center" />
      <el-table-column label="访客手机号" prop="visitorPhone" align="center" />
      <el-table-column label="访客身份证号" prop="visitorIdCard" align="center" />
      <el-table-column label="访问事由" prop="visitReason" align="center" />
      <el-table-column label="预约部门" prop="deptName" align="center" />
      <el-table-column label="对接人" prop="contactUserName" align="center" />
      <el-table-column label="预约开始时间" prop="startTime" align="center" width="180" />
      <el-table-column label="预约结束时间" prop="endTime" align="center" width="180" />
      <el-table-column label="预约状态" prop="status" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">待确认</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已确认</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="danger">已拒绝</el-tag>
          <el-tag v-else-if="scope.row.status === '3'" type="info">已完成</el-tag>
          <el-tag v-else-if="scope.row.status === '4'" type="default">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="拒绝原因" prop="rejectReason" align="center" />
      <el-table-column label="创建时间" prop="createTime" align="center" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            v-hasPermi="['system:visitor-appointment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:visitor-appointment:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增或修改访客预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="访客姓名" prop="visitorName">
          <el-input v-model="form.visitorName" placeholder="请输入访客姓名" />
        </el-form-item>
        <el-form-item label="访客手机号" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" placeholder="请输入访客手机号" />
        </el-form-item>
        <el-form-item label="访客身份证号" prop="visitorIdCard">
          <el-input v-model="form.visitorIdCard" placeholder="请输入访客身份证号" />
        </el-form-item>
        <el-form-item label="访问事由" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入访问事由" />
        </el-form-item>
        <el-form-item label="预约部门ID" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入预约部门ID" />
        </el-form-item>
        <el-form-item label="预约部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入预约部门名称" />
        </el-form-item>
        <el-form-item label="对接人ID" prop="contactUserId">
          <el-input v-model="form.contactUserId" placeholder="请输入对接人ID" />
        </el-form-item>
        <el-form-item label="对接人姓名" prop="contactUserName">
          <el-input v-model="form.contactUserName" placeholder="请输入对接人姓名" />
        </el-form-item>
        <el-form-item label="预约开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择预约开始时间" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="预约结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="请选择预约结束时间" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 拒绝预约对话框 -->
    <el-dialog title="拒绝预约" :visible.sync="rejectOpen" width="400px" append-to-body>
      <el-form ref="rejectForm" :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因" prop="rejectReason">
          <el-input v-model="rejectForm.rejectReason" type="textarea" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitReject">确 定</el-button>
        <el-button @click="cancelReject">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVisitorAppointment, getVisitorAppointment, addVisitorAppointment, updateVisitorAppointment, delVisitorAppointment, confirmVisitorAppointment, rejectVisitorAppointment } from "@/api/system/visitor-appointment";

export default {
  name: "VisitorAppointment",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 访客预约列表
      appointmentList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitorName: null,
        deptId: null,
        status: null
      },
      // 表单参数
      form: {},
      // 拒绝表单参数
      rejectForm: {},
      // 对话框标题
      title: "",
      // 弹出层标题
      open: false,
      // 拒绝弹出层标题
      rejectOpen: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询访客预约列表 */
    getList() {
      this.loading = true;
      listVisitorAppointment(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.appointmentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消拒绝按钮
    cancelReject() {
      this.rejectOpen = false;
      this.resetReject();
    },
    // 表单重置
    reset() {
      this.form = {
        appointmentId: null,
        visitorName: null,
        visitorPhone: null,
        visitorIdCard: null,
        visitReason: null,
        deptId: null,
        deptName: null,
        contactUserId: null,
        contactUserName: null,
        startTime: null,
        endTime: null,
        status: null,
        rejectReason: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    // 拒绝表单重置
    resetReject() {
      this.rejectForm = {
        rejectReason: null
      };
      this.resetForm("rejectForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.appointmentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增访客预约";
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset();
      const appointmentId = row.appointmentId || this.ids
      getVisitorAppointment(appointmentId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改访客预约";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const appointmentIds = row.appointmentId || this.ids;
      this.$confirm('是否确认删除访客预约编号为"' + appointmentIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delVisitorAppointment(appointmentIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 确认按钮操作 */
    handleConfirm(row) {
      const appointmentIds = row.appointmentId || this.ids;
      this.$confirm('是否确认预约编号为"' + appointmentIds + '"的预约?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "info"
      }).then(function() {
        return confirmVisitorAppointment(appointmentIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("确认成功");
      }).catch(() => {});
    },
    /** 拒绝按钮操作 */
    handleReject(row) {
      const appointmentIds = row.appointmentId || this.ids;
      this.rejectOpen = true;
      this.rejectForm.appointmentId = appointmentIds;
    },
    /** 提交拒绝操作 */
    submitReject() {
      rejectVisitorAppointment(this.rejectForm.appointmentId, this.rejectForm.rejectReason).then(() => {
        this.rejectOpen = false;
        this.msgSuccess("拒绝成功");
        this.getList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.appointmentId != null) {
            updateVisitorAppointment(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVisitorAppointment(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
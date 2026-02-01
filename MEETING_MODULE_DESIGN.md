# 多功能厅预约模块设计方案

## 项目概述

基于RuoYi-Vue-Plus框架开发的多功能厅预约管理系统，实现了会议室预约、权限控制、审批流程、冲突检测、统计分析等核心功能。

## 一、数据表核心字段与状态流转逻辑

### 1.1 数据表结构

#### 会议室表 (meeting_room)
```sql
- room_id: 会议室ID (主键)
- room_name: 会议室名称
- room_type: 会议室类型 (L大型 S小型)
- capacity: 容纳人数
- location: 位置描述
- equipment: 设备配置
- status: 状态 (0正常 1停用)
- del_flag: 删除标志
```

#### 预约记录表 (meeting_appointment)
```sql
- appointment_id: 预约ID (主键)
- appointment_no: 预约单号
- room_id: 会议室ID (外键)
- user_id: 预约用户ID
- user_name: 预约用户姓名
- dept_id: 用户部门ID
- dept_name: 用户部门名称
- meeting_title: 会议主题
- meeting_desc: 会议描述
- start_time: 开始时间
- end_time: 结束时间
- attendees: 参会人数
- status: 状态 (01草稿 02已提交 03部门审核中 04行政复核中 05已批准 06已拒绝 07已取消)
- cancel_reason: 取消原因
- cancel_time: 取消时间
- submit_time: 提交时间
```

#### 审批流程记录表 (meeting_approval)
```sql
- approval_id: 审批ID (主键)
- appointment_id: 预约ID (外键)
- appointment_no: 预约单号
- approval_level: 审批级别 (1部门审批 2行政审批)
- approver_id: 审批人ID
- approver_name: 审批人姓名
- approver_dept_id: 审批人部门ID
- approver_role: 审批人角色
- approval_status: 审批状态 (01待审批 02已通过 03已拒绝)
- approval_opinion: 审批意见
- approval_time: 审批时间
```

### 1.2 状态流转逻辑

```
草稿(01) → 已提交(02) → 部门审核中(03) → 行政复核中(04) → 已批准(05)
                                    ↓
                                已拒绝(06) ← 任一环节拒绝
                                    
已批准(05) → 已取消(07) ← 用户主动取消
```

状态流转规则：
1. 草稿状态：用户可以修改和删除
2. 已提交状态：进入审批流程，不可修改
3. 部门审核中：直属上级审批
4. 行政复核中：行政部人员审批
5. 已批准：预约成功，可以使用会议室
6. 已拒绝：审批被拒绝，流程终止
7. 已取消：用户主动取消预约

## 二、核心权限校验规则

### 2.1 大型厅权限校验

**规则**：大型厅仅限部门总监及以上权限人员申请

**实现逻辑**：
```java
public boolean hasLargeRoomPermission(Long userId) {
    // 超级管理员有权限
    if (LoginHelper.isSuperAdmin(userId)) {
        return true;
    }
    
    // 检查是否为总监及以上级别
    return isDirectorOrAbove(userId);
}

private boolean isDirectorOrAbove(Long userId) {
    SysUser user = userService.selectUserById(userId);
    if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
        return false;
    }
    
    // 检查角色权限标识
    List<String> DIRECTOR_ROLES = Arrays.asList("director", "manager", "ceo", "coo");
    return user.getRoles().stream()
        .anyMatch(role -> DIRECTOR_ROLES.contains(role.getRoleKey()));
}
```

### 2.2 24小时内不可取消规则

**规则**：大型厅在会议开始前24小时内不可取消

**实现逻辑**：
```java
public boolean checkLargeRoomCancelRestriction(Long appointmentId) {
    MeetingAppointment appointment = appointmentMapper.selectById(appointmentId);
    if (appointment == null) {
        return false;
    }
    
    // 获取会议室信息
    MeetingRoom room = roomMapper.selectById(appointment.getRoomId());
    if (room == null || !MeetingRoomType.LARGE.getCode().equals(room.getRoomType())) {
        // 不是大型厅，可以取消
        return true;
    }
    
    // 检查是否在会议开始前24小时内
    Date now = new Date();
    Date startTime = appointment.getStartTime();
    
    // 计算24小时前的时间
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startTime);
    calendar.add(Calendar.HOUR_OF_DAY, -24);
    Date twentyFourHoursBefore = calendar.getTime();
    
    // 如果当前时间距离会议开始时间不足24小时，则不可取消
    return now.before(twentyFourHoursBefore);
}
```

### 2.3 待审批预约数量限制

**规则**：同一用户在同一时间段内，最多只能有2个待审批的预约

**实现逻辑**：
```java
public boolean checkMaxPendingAppointments(Long userId) {
    // 获取用户待审批的预约数量（包括已提交、部门审核中、行政复核中状态）
    List<String> pendingStatuses = Arrays.asList(
        AppointmentStatus.SUBMITTED.getCode(),
        AppointmentStatus.DEPT_APPROVING.getCode(),
        AppointmentStatus.ADMIN_APPROVING.getCode()
    );
    
    long count = appointmentMapper.selectCount(
        appointmentMapper.lambdaQuery()
            .eq(MeetingAppointment::getUserId, userId)
            .in(MeetingAppointment::getStatus, pendingStatuses)
            .eq(MeetingAppointment::getDelFlag, "0")
    );
    
    return count < MAX_PENDING_APPOINTMENTS; // MAX_PENDING_APPOINTMENTS = 2
}
```

## 三、预约成功率统计接口核心查询逻辑

### 3.1 统计需求
- 按会议室类型统计本周的预约成功率
- 成功率 = 已批准数 / 总申请数

### 3.2 核心查询逻辑

```java
public List<MeetingStatisticsVo> getStatistics() {
    // 获取本周的开始和结束时间
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date weekStart = calendar.getTime();
    
    calendar.add(Calendar.DAY_OF_WEEK, 7);
    Date weekEnd = calendar.getTime();
    
    // 查询本周的预约统计（按会议室分组）
    List<Map<String, Object>> statistics = baseMapper.selectMaps(
        new LambdaQueryWrapper<MeetingAppointment>()
            .ge(MeetingAppointment::getCreateTime, weekStart)
            .le(MeetingAppointment::getCreateTime, weekEnd)
            .eq(MeetingAppointment::getDelFlag, "0")
            .groupBy(MeetingAppointment::getRoomId)
    );
    
    // 按会议室类型聚合统计
    Map<String, MeetingStatisticsVo> typeStatistics = new HashMap<>();
    
    for (Map<String, Object> stat : statistics) {
        Long roomId = (Long) stat.get("room_id");
        Long totalCount = (Long) stat.get("total_count");
        Long approvedCount = (Long) stat.get("approved_count");
        
        MeetingRoom room = roomMapper.selectById(roomId);
        if (room != null) {
            String roomType = room.getRoomType();
            MeetingStatisticsVo typeStat = typeStatistics.computeIfAbsent(roomType, k -> {
                MeetingStatisticsVo vo = new MeetingStatisticsVo();
                vo.setRoomType(roomType);
                vo.setRoomTypeName(MeetingRoomType.getByCode(roomType).getInfo());
                vo.setTotalApplications(0L);
                vo.setApprovedCount(0L);
                return vo;
            });
            
            typeStat.setTotalApplications(typeStat.getTotalApplications() + totalCount);
            typeStat.setApprovedCount(typeStat.getApprovedCount() + approvedCount);
        }
    }
    
    // 计算成功率
    List<MeetingStatisticsVo> result = new ArrayList<>(typeStatistics.values());
    for (MeetingStatisticsVo vo : result) {
        if (vo.getTotalApplications() > 0) {
            double successRate = (double) vo.getApprovedCount() / vo.getTotalApplications() * 100;
            vo.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
        } else {
            vo.setSuccessRate(0.0);
        }
    }
    
    return result;
}
```

### 3.3 SQL查询示例

```sql
-- 按会议室类型统计本周预约数据
SELECT 
    r.room_type,
    COUNT(*) as total_applications,
    SUM(CASE WHEN a.status = '05' THEN 1 ELSE 0 END) as approved_count,
    ROUND(SUM(CASE WHEN a.status = '05' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) as success_rate
FROM meeting_appointment a
JOIN meeting_room r ON a.room_id = r.room_id
WHERE a.create_time >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)
  AND a.create_time < DATE_ADD(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 7 DAY)
  AND a.del_flag = '0'
GROUP BY r.room_type;
```

## 四、系统架构设计

### 4.1 模块结构
```
ruoyi-meeting/
├── src/main/java/org/dromara/meeting/
│   ├── config/           # 配置类
│   ├── controller/       # 控制器层
│   ├── domain/          # 实体类
│   │   ├── bo/         # 业务对象
│   │   └── vo/         # 视图对象
│   ├── enums/           # 枚举类
│   ├── mapper/          # 数据访问层
│   └── service/         # 业务逻辑层
│       └── impl/        # 实现类
└── src/main/resources/
    └── mapper/          # MyBatis映射文件
```

### 4.2 核心服务

1. **权限校验服务 (IPermissionCheckService)**
   - 大型厅权限校验
   - 用户预约数量限制校验
   - 角色权限检查

2. **冲突检测服务 (IConflictCheckService)**
   - 会议室时间冲突检测
   - 大型厅24小时取消限制检查

3. **预约服务 (IMeetingAppointmentService)**
   - 预约CRUD操作
   - 状态流转控制
   - 提交和取消功能

4. **审批服务 (IMeetingApprovalService)**
   - 部门审批
   - 行政审批
   - 审批记录查询

### 4.3 API接口

#### 会议室管理
- `GET /meeting/room/list` - 查询会议室列表
- `GET /meeting/room/{roomId}` - 获取会议室详情
- `POST /meeting/room` - 新增会议室
- `PUT /meeting/room` - 修改会议室
- `DELETE /meeting/room/{roomIds}` - 删除会议室

#### 预约管理
- `GET /meeting/appointment/list` - 查询预约列表
- `GET /meeting/appointment/{appointmentId}` - 获取预约详情
- `POST /meeting/appointment` - 新增预约
- `PUT /meeting/appointment` - 修改预约
- `POST /meeting/appointment/submit/{appointmentId}` - 提交预约
- `POST /meeting/appointment/cancel/{appointmentId}` - 取消预约
- `GET /meeting/appointment/statistics` - 获取统计信息

#### 审批管理
- `POST /meeting/approval/dept/{appointmentId}` - 部门审批
- `POST /meeting/approval/admin/{appointmentId}` - 行政审批
- `GET /meeting/approval/records/{appointmentId}` - 获取审批记录

## 五、安全与权限控制

### 5.1 权限标识
```
meeting:room:list        - 查询会议室列表
meeting:room:add         - 新增会议室
meeting:room:edit        - 修改会议室
meeting:room:remove      - 删除会议室
meeting:room:export      - 导出会议室数据

meeting:appointment:list     - 查询预约列表
meeting:appointment:add      - 新增预约
meeting:appointment:edit     - 修改预约
meeting:appointment:remove   - 删除预约
meeting:appointment:submit   - 提交预约
meeting:appointment:cancel   - 取消预约
meeting:appointment:export   - 导出预约数据
meeting:appointment:statistics - 查看统计信息

meeting:approval:dept     - 部门审批权限
meeting:approval:admin    - 行政审批权限
meeting:approval:list     - 查看审批记录
```

### 5.2 数据权限
- 用户只能查看和操作自己的预约记录
- 部门领导可以审批本部门员工的预约
- 行政人员可以进行最终审批
- 超级管理员拥有所有权限

## 六、部署与配置

### 6.1 数据库初始化
执行 `meeting_room_module.sql` 文件创建数据表和初始数据。

### 6.2 模块配置
在 `meeting.yml` 中配置相关参数：
```yaml
meeting:
  max-pending-appointments: 2
  large-room-cancel-restriction: true
```

### 6.3 权限配置
在系统管理中添加相应的菜单和权限标识。

## 七、测试验证

### 7.1 单元测试
- 枚举类测试
- 权限校验测试
- 冲突检测测试
- 控制器测试

### 7.2 集成测试
- 完整预约流程测试
- 权限控制测试
- 并发冲突测试
- 统计功能测试

## 八、性能优化建议

1. **数据库优化**
   - 为常用查询字段添加索引
   - 使用分页查询避免大数据量查询
   - 定期清理历史数据

2. **缓存优化**
   - 会议室信息缓存
   - 用户权限信息缓存
   - 统计结果缓存

3. **查询优化**
   - 使用JOIN查询减少数据库访问次数
   - 合理使用分页和排序
   - 避免N+1查询问题

## 九、扩展功能建议

1. **消息通知**
   - 预约状态变更通知
   - 审批提醒通知
   - 会议开始前提醒

2. **移动端支持**
   - 移动端预约界面
   - 移动端审批功能

3. **高级功能**
   - 会议室使用率分析
   - 部门使用统计
   - 预约冲突智能推荐

4. **集成扩展**
   - 与企业日历集成
   - 与视频会议系统集成
   - 与门禁系统集成
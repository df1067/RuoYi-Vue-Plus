import request from '@/utils/request'

// 查询访客预约列表
export function listVisitorAppointment(query) {
  return request({
    url: '/system/visitor-appointment/list',
    method: 'get',
    params: query
  })
}

// 查询访客预约详细
export function getVisitorAppointment(appointmentId) {
  return request({
    url: '/system/visitor-appointment/' + appointmentId,
    method: 'get'
  })
}

// 新增访客预约
export function addVisitorAppointment(data) {
  return request({
    url: '/system/visitor-appointment',
    method: 'post',
    data: data
  })
}

// 修改访客预约
export function updateVisitorAppointment(data) {
  return request({
    url: '/system/visitor-appointment',
    method: 'put',
    data: data
  })
}

// 确认访客预约
export function confirmVisitorAppointment(appointmentId) {
  return request({
    url: '/system/visitor-appointment/confirm/' + appointmentId,
    method: 'put'
  })
}

// 拒绝访客预约
export function rejectVisitorAppointment(appointmentId, rejectReason) {
  return request({
    url: '/system/visitor-appointment/reject/' + appointmentId,
    method: 'put',
    params: { rejectReason: rejectReason }
  })
}

// 删除访客预约
export function delVisitorAppointment(appointmentId) {
  return request({
    url: '/system/visitor-appointment/' + appointmentId,
    method: 'delete'
  })
}
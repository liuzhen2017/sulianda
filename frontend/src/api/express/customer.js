import request from '@/utils/request'

// 查询快递客户列表
export function listCustomer(query) {
  return request({
    url: '/express/customer/list',
    method: 'get',
    params: query
  })
}

// 查询快递客户详细
export function getCustomer(id) {
  return request({
    url: '/express/customer/' + id,
    method: 'get'
  })
}

// 新增快递客户
export function addCustomer(data) {
  return request({
    url: '/express/customer',
    method: 'post',
    data: data
  })
}

// 修改快递客户
export function updateCustomer(data) {
  return request({
    url: '/express/customer',
    method: 'put',
    data: data
  })
}

// 删除快递客户
export function delCustomer(id) {
  return request({
    url: '/express/customer/' + id,
    method: 'delete'
  })
}

// 下载快递客户导入模板
export function importCustomerTemplate() {
  return request({
    url: '/express/customer/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}

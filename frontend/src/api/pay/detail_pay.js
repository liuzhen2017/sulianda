import request from '@/utils/request'

// 查询账户充值明细列表
export function listPay(query) {
  return request({
    url: '/pay/pay/list',
    method: 'get',
    params: query
  })
}

// 查询账户充值明细详细
export function getPay(id) {
  return request({
    url: '/pay/pay/' + id,
    method: 'get'
  })
}

// 新增账户充值明细
export function addPay(data) {
  return request({
    url: '/pay/pay',
    method: 'post',
    data: data
  })
}

// 修改账户充值明细
export function updatePay(data) {
  return request({
    url: '/pay/pay',
    method: 'put',
    data: data
  })
}

// 删除账户充值明细
export function delPay(id) {
  return request({
    url: '/pay/pay/' + id,
    method: 'delete'
  })
}

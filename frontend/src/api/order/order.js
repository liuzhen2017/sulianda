import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/order/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(id) {
  return request({
    url: '/order/order/' + id,
    method: 'get'
  })
}

// 新增订单
export function addOrder(data) {
  return request({
    url: '/order/order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/order/order',
    method: 'put',
    data: data
  })
}

// 打印面单
export function printInfo(id) {
  return request({
    url: '/order/order/printInfo/' + id,
    method: 'get',
  })
}

// 根据快递公司选择运输方式
export function getShippingMethod(id) {
  return request({
    url: '/express/express/shipping-method/?id='+id,
    method: 'get',
  })
}

// 操作订单骨架状态
export function operateOrder(data) {
  return request({
    url: '/order/order/operater',
    method: 'put',
    data: data
  })
}

// 查询订单业务轨迹
export function listOrderEvents(id) {
  return request({
    url: '/order/order/' + id + '/events',
    method: 'get'
  })
}

// 删除订单
export function delOrder(id) {
  return request({
    url: '/order/order/' + id,
    method: 'delete'
  })
}

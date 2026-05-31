import request from '@/utils/request'

// 查询快递列表
export function listExpress(query) {
  return request({
    url: '/express/express/list',
    method: 'get',
    params: query
  })
}

// 查询快递详细
export function getExpress(id) {
  return request({
    url: '/express/express/' + id,
    method: 'get'
  })
}

// 新增快递
export function addExpress(data) {
  return request({
    url: '/express/express',
    method: 'post',
    data: data
  })
}

// 修改快递
export function updateExpress(data) {
  return request({
    url: '/express/express',
    method: 'put',
    data: data
  })
}

// 删除快递
export function delExpress(id) {
  return request({
    url: '/express/express/' + id,
    method: 'delete'
  })
}

import request from '@/utils/request'


// 查询
export function lineChart() {
  return request({
    url: '/chart-index/',
    method: 'get'
  })
}
// 查询盈利折list
export function getProfitList(query) {
  return request({
    url: '/chart-index/get-profit',
    method: 'get',
    params: query
  })
}

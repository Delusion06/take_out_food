// 查询列表页接口
const getOrderDetailPage = (params) => {
  return $axios({
    url: '/orders/employee/page',
    method: 'get',
    params
  })
}

// 取消，派送，完成接口
const editOrderDetail = (params) => {
  return $axios({
    url: '/orders/editOrdersStatus',
    method: 'post',
    data: { ...params }
  })
}

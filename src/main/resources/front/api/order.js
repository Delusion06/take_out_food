//提交订单
function addOrderApi(data) {
    return $axios({
        url: '/alipay/buyOrder',
        method: 'get',
        data
    })
}

//查询所有订单
function orderListApi() {
    return $axios({
        url: '/order/list',
        method: 'get',
    })
}

//分页查询订单
function orderPagingApi(data) {
    return $axios({
        url: '/orders/user/page',
        method: 'get',
        params: {...data}
    })
}

//再来一单
function orderAgainApi(data) {
    return $axios({
        'url': '/orders/again',
        'method': 'post',
        data
    })
}

//去支付
function addShoppingCartToOrders() {
    return $axios({
        url: '/orders/addShoppingCartToOrders',
        method: 'post',
    })
}

function deleteOrdersToShoppingCartApi(data){
    return $axios({
        url: '/orders/deleteOrdersToShoppingCart',
        method: 'post',
        data
    })
}

function getOrdersByIdApi(data) {
    return $axios({
        url: '/orders/getById',
        method: 'post',
        data
    })
}

function editOrdersStatusApi(data){
    return $axios({
        url: '/orders/user/editOrdersStatus',
        method: 'post',
        data
    })
}
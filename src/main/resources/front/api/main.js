//获取所有的菜品分类
function categoryListApi(params) {
    return $axios({
        'url': '/category/list',
        'method': 'post',
        data: params
    })
}

//获取菜品分类对应的菜品
function dishListApi(params) {
    return $axios({
        'url': '/dish/list',
        'method': 'post',
        data: params
    })
}

//获取菜品分类对应的套餐
function comboListApi(params) {
    return $axios({
        'url': '/combo/list',
        'method': 'post',
        data: {...params}
    })
}

//获取购物车内商品的集合
function shoppingCartListApi() {
    return $axios({
        'url': '/shoppingCart/list',
        'method': 'post',
    })
}

//购物车中添加商品
function addCartApi(params) {
    return $axios({
        url: '/shoppingCart/add',
        method: 'post',
        data: {...params}
    })
}

//购物车中修改商品
function updateCartApi(params) {
    return $axios({
        url: '/shoppingCart/sub',
        method: 'post',
        data: {...params}
    })
}

//删除购物车的商品
function clearCartApi() {
    return $axios({
        url: '/shoppingCart/clean',
        method: 'post',
    })
}

//获取套餐的全部菜品
function getComboByIdApi(data) {
    return $axios({
        url: '/combo/getById',
        method: 'post',
        data
    })
}

function requestUrlApi(argName) {
    var url = location.href
    var arrStr = url.substring(url.indexOf("?") + 1).split("&")
    for (var i = 0; i < arrStr.length; i++) {
        var loc = arrStr[i].indexOf(argName + "=")
        if (loc !== -1) {
            return arrStr[i].replace(argName + "=", "").replace("?", "")
        }
    }
    return ""
}


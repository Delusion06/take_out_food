//获取所有地址
function addressListApi() {
    return $axios({
        'url': '/address/list',
        'method': 'post',
    })
}

//新增地址
function addAddressApi(data) {
    return $axios({
        'url': '/address/add',
        'method': 'post',
        data
    })
}

//修改地址
function updateAddressApi(data) {
    return $axios({
        'url': '/address/edit',
        'method': 'post',
        data
    })
}

function deleteAddressApi(data) {
    return $axios({
        url: '/address/delete',
        method: 'post',
        data
    })
}

function addressFindOneApi(params) {
    return $axios({
        url: '/address/getById',
        method: 'post',
        data: params
    })
}

function setDefaultAddressApi(params) {
    return $axios({
        url: '/address/editDefault',
        method: 'post',
        data: params
    })
}

function getDefaultAddressApi() {
    return $axios({
        url: '/address/default',
        method: 'post',
    })
}
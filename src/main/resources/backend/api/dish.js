function getDishPage(params) {
    return $axios({
        url: '/dish/page',
        method: 'get',
        params
    })
}

function getDishById(params) {
    return $axios({
        url: '/dish/getById',
        method: 'post',
        data: {...params}
    })
}

function addDish(params) {
    return $axios({
        url: '/dish/add',
        method: 'post',
        data: {...params}
    })
}

function editDish(params) {
    return $axios({
        url: '/dish/edit',
        method: 'post',
        data: {...params}
    })
}

function editDishStatus(params) {
    return $axios({
        url: '/dish/editStatus',
        method: 'post',
        data: params
    })
}

function deleteDish(params) {
    return $axios({
        url: '/dish/delete',
        method: 'post',
        data: params
    })
}

function getCategoryList(params) {
    return $axios({
        url: '/category/list',
        method: 'post',
        data: params
    })
}

function getDishList(params) {
    return $axios({
        url: '/dish/list',
        method: 'post',
        data: params
    })
}

function commonDownload(params) {
    return $axios({
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        url: '/common/download',
        method: 'get',
        params
    })
}
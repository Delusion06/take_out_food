function getCategoryPage(params) {
    return $axios({
        url: '/category/page',
        method: 'get',
        params
    })
}

function addCategory(params) {
    return $axios({
        url: '/category/add',
        method: 'post',
        data: {...params}
    })
}

function editCategory(params) {
    return $axios({
        url: '/category/edit',
        method: 'post',
        data: {...params}
    })
}

function deleteCategory(params) {
    return $axios({
        url: '/category/delete',
        method: 'post',
        data: {...params}
    })
}
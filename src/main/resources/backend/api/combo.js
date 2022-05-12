function getComboPage(params) {
    return $axios({
        url: '/combo/page',
        method: 'get',
        params
    })
}

function getComboById(params) {
    return $axios({
        url: '/combo/getById',
        method: 'post',
        data: {...params}
    })
}

function addCombo(params) {
    return $axios({
        url: '/combo/add',
        method: 'post',
        data: {...params}
    })
}

function editCombo(params){
    return $axios({
        url: '/combo/edit',
        method: 'post',
        data: params
    })
}

function editComboStatus(params) {
    return $axios({
        url: '/combo/editStatus',
        method: 'post',
        data: params
    })
}

function deleteCombo(params) {
    return $axios({
        url: '/combo/delete',
        method: 'post',
        data: params
    })
}



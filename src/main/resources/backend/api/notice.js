function getNoticePage(params) {
    return $axios({
        url: '/notice/page',
        method: 'get',
        params
    })
}

function getNoticeById(params) {
    return $axios({
        url: '/notice/getById',
        method: 'post',
        data: {...params}
    })
}

function addNotice(params) {
    return $axios({
        url: '/notice/add',
        method: 'post',
        data: {...params}
    })
}

function editNotice(params) {
    return $axios({
        url: '/notice/edit',
        method: 'post',
        data: {...params}
    })
}

function editStatus(params) {
    return $axios({
        url: '/notice/editStatus',
        method: 'post',
        data: {...params}
    })
}
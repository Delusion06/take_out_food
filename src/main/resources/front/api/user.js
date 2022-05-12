function loginApi(data) {
    return $axios({
        url: '/user/login',
        method: 'post',
        data
    })
}

function sendMsgApi(data) {
    return $axios({
        url: '/user/sendMsg',
        method: 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        url: '/user/logout',
        method: 'post',
    })
}

function registerApi(data) {
    return $axios({
        url: '/user/register',
        method: 'post',
        data
    })
}

function getUserById() {
    return $axios({
        url: '/user/getById',
        method: 'post',
    })
}

function editPasswordApi(data) {
    return $axios({
        url: '/user/editPassword',
        method: 'post',
        data
    })
}

function editUserApi(data) {
    return $axios({
        url: '/user/editUser',
        method: 'post',
        data
    })
}
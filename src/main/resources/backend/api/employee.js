function getEmployeePage(params) {
    return $axios({
        url: '/employee/page',
        method: 'get',
        params
    })
}

function getEmployeeById(params) {
    return $axios({
        url: '/employee/getById',
        method: 'post',
        data: {...params}
    })
}

function addEmployee(params) {
    return $axios({
        url: '/employee/add',
        method: 'post',
        data: {...params}
    })
}

function editEmployee(params) {
    return $axios({
        url: '/employee/edit',
        method: 'post',
        data: {...params}
    })
}
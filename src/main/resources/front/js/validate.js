function isCellPhone(val) {
    if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
        return false
    } else {
        return true
    }
}

function validatePhone(rule, value, callback) {
    if (value === "") {
        callback(new Error("请输入手机号!"))
    } else if (!isCellPhone(value)) {
        callback(new Error("请输入正确的手机号!"))
    } else {
        callback()
    }
}

function validatePassword(rule, value, callback) {
    if (value.length < 6) {
        callback(new Error('密码必须在6位以上'))
    } else {
        callback()
    }
}

function validateUsername(rule, value, callback) {
    if (value.length < 1) {
        callback(new Error('请输入用户名'))
    } else if (value.length > 4){
        callback(new Error('姓名不得超过4位数'))
    } else {
        callback()
    }
}

function validID(rule, value, callback) {
    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
    if (value == '') {
        callback(new Error('请输入身份证号码'))
    } else if (reg.test(value)) {
        callback()
    } else {
        callback(new Error('身份证号码不正确'))
    }
}
import request from '@/utils/request.js'

let prefix = '/auth'
export function login(data) {
    return request({
        url: prefix + '/login',
        method: 'POST',
        data
    })
}

export function registerUser(data) {
    console.log(data)
    return request({
        url: prefix + '/register',
        method: 'POST',
        data
    })
}
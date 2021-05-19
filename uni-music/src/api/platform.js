import request from '@/utils/request.js'

let prefix = '/platform'
export function recommend(data) {
    return request({
        url: prefix + '/recommend',
        method: 'GET',
        data
    })
}

export function search(data) {
    return request({
        url: prefix + '/search',
        method: 'GET',
        data
    })
}
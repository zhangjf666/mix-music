import request from '@/utils/request.js'

let prefix = '/user'
export function userConfig(data) {
    return request({
        url: prefix + '/config',
        method: 'PUT',
        data,
        header: {'content-type': 'application/json'}
    })
}
import request from '@/utils/request.js'

let prefix = '/platform'
export function recommendSongList(data) {
    return request({
        url: prefix + '/recommend',
        method: 'GET',
        data
    })
}
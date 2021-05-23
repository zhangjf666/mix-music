import request from '@/utils/request.js'

let prefix = '/platform'
export function recommend(data) {
    return request({
        url: prefix + '/recommend',
        method: 'GET',
        data
    })
}

export function recommendNewSong(data) {
    return request({
        url: prefix + '/recommend/newsong',
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

export function banner() {
    var data = {t: Date.parse(new Date())}
    return request({
        url: prefix + '/banner',
        method: 'GET',
        data
    })
}

export function url(data) {
    return request({
        url: prefix + '/url',
        method: 'GET',
        data
    })
}

export function lyric(data) {
    return request({
        url: prefix + '/lyric',
        method: 'GET',
        data
    })
}
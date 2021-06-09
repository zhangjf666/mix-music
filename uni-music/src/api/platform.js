import request from '@/utils/request.js'

let prefix = '/platform'
export function recommendSongList(data) {
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
//搜索
export function search(data) {
    return request({
        url: prefix + '/search',
        method: 'GET',
        data
    })
}
//默认搜索关键词
export function searchDefaultKeyword(data) {
    var data = {}
    return request({
        url: prefix + '/searchDefaultKeyword',
        method: 'GET',
        data
    })
}

//热搜列表
export function searchHotDetail(data) {
    var data = {}
    return request({
        url: prefix + '/searchHotDetail',
        method: 'GET',
        data
    })
}

//搜索建议
export function searchSuggest(data) {
    return request({
        url: prefix + '/searchSuggest',
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

export function songUrl(data) {
    return request({
        url: prefix + '/url',
        method: 'GET',
        data
    })
}

export function songLyric(data) {
    return request({
        url: prefix + '/lyric',
        method: 'GET',
        data
    })
}

export function songListDetail(data) {
    return request({
        url: prefix + '/playlistDetail',
        method: 'GET',
        data
    })
}

export function allTags() {
    var data = {}
    return request({
        url: prefix + '/allTags',
        method: 'GET',
        data
    })
}

export function highQualityTags() {
    var data = {}
    return request({
        url: prefix + '/highQualityTags',
        method: 'GET',
        data
    })
}

export function hotTags() {
    var data = {}
    return request({
        url: prefix + '/hotTags',
        method: 'GET',
        data
    })
}

export function highQualityList(data) {
    return request({
        url: prefix + '/highQualityList',
        method: 'GET',
        data
    })
}

export function categoryList(data) {
    return request({
        url: prefix + '/categoryList',
        method: 'GET',
        data
    })
}

export function topListDetail(data) {
    var data = {}
    return request({
        url: prefix + '/topListDetail',
        method: 'GET',
        data
    })
}

export function songInfo(data) {
    return request({
        url: prefix + '/song',
        method: 'GET',
        data
    })
}
//新歌速递
export function topSong(data) {
    return request({
        url: prefix + '/topSong',
        method: 'GET',
        data
    })
}
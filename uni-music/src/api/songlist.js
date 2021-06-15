import request from '@/utils/request.js'

let prefix = '/songlist'
//获取用户歌单列表
export function userSonglist(data) {
    return request({
        url: prefix,
        method: 'GET',
        data
    })
}

// 创建歌单
export function createSonglist(data) {
    return request({
        url: prefix,
        method: 'POST',
        data
    })
}

// 更新歌单
export function updateSonglist(data) {
    return request({
        url: prefix,
        method: 'PUT',
        data
    })
}

// 删除歌单
export function deleteSonglist(data) {
    return request({
        url: prefix,
        method: 'DELETE',
        data
    })
}

// 获取歌单歌曲详情
export function getSonglistDetail(data) {
    return request({
        url: prefix + '/detail',
        method: 'GET',
        data
    })
}

//歌单添加歌曲
export function addSong(data) {
    return request({
        url: prefix + '/addSong',
        method: 'POST',
        data
    })
}

//歌单删除歌曲
export function delSong(data) {
    return request({
        url: prefix + '/delSong',
        method: 'POST',
        data
    })
}

//更新歌单歌曲
export function updateSongs(data) {
    return request({
        url: prefix + '/updateSongs',
        method: 'POST',
        data
    })
}
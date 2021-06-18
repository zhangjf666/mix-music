import Vue from 'vue'
import Vuex from 'vuex'
import { songUrl } from '../api/platform'
import { userSonglist, createSonglist, updateSonglist, deleteSonglist, getSonglistDetail, addSong, delSong, updateSongs, existSong} from '../api/songlist'
import { userConfig } from '../api/user'

Vue.use(Vuex);

//生成从minNum到maxNum的随机整数
function randomNum(minNum,maxNum){ 
    switch(arguments.length){ 
        case 1: 
            return parseInt(Math.random()*minNum+1,10); 
        break; 
        case 2: 
            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); 
        break; 
        default: 
            return 0; 
        break; 
    } 
} 

//查找元素下标
function indexOf(arr, item) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].id === item.id) {
            return i;
        }
    }
    return -1;
}

// 挂载一个可全局访问的音频组件
let audio = uni.createInnerAudioContext();

audio.autoplay = true;
// 音频就绪后触发的回调函数
audio.onCanplay(() => {
	let time = audio.duration;
	store.commit('setTotalTime', time);
	let m = parseInt(time / 60);
	m = m < 10 ? '0' + m : m;
	let s = parseInt(time % 60);
	s = s < 10 ? '0' + s : s;
	time = m + ':' + s;
	// store.commit('getEndTime', time);
})
// 音频进度更新后触发的时间
audio.onTimeUpdate(() => {
	let nowTime = audio.currentTime;
	store.commit('setCurrentTime', nowTime);
	let m = parseInt(nowTime / 60);
	m = m < 10 ? '0' + m : m;
	let s = parseInt(nowTime % 60);
	s = s < 10 ? '0' + s : s;
	let t = m + ':' + s;
	// store.commit('getnowPlayTime', t);
})
// 音频自然播放结束后触发的事件
audio.onEnded(() => {
	store.commit('playNext');
})

const store = new Vuex.Store({
    state: {
        // 当前播放的音乐的索引
        playingIndex: null,
        // 播放列表
        playlist: [],
        // 控制播放
        isPlay: false,
        // 控制播放列表显示隐藏
        isShowPlaylist: false,
        // 控制播放器页面显示隐藏
        isShowPlayPage: false,
        // 音频总时长(音频组件)
		totalTime: null,
        // 音频总时长(显示)
		endTime: '00:00',
        // 当前播放时长(显示)
        playTime: '00:00',
        // 音量
		volume:50,
        // 当前播放时间(音频组件)
		currentTime:0,
        // 播放模式(1单曲,2列表,3随机)
        playMode: 2,
        // 播放器
        audio: audio,
        // 播放控件需要的前一曲,当前歌曲,后一曲
        playSongGroup: [],
        // 播放swiper控件当前item位置id
        playSwiperItemId: 'pre',
        // token
        token: '',
        // user
        user: {},
        // 我喜欢的歌单
        favouriteList: {},
        // 创建的歌单
        createList: [],
        // 收藏的歌单
        collectList: []
    },
    getters: {
        playlistLength(state) {
            return state.playlist.length;
        },
        playModeText(state) {
            return state.playMode == 3 ? '随机播放' : state.playMode == 2 ? '列表循环' : '单曲循环';
        },
        playModeIcon(state) {
            return state.playMode == 3 ? 'icon-random-loop' : state.playMode == 2 ? 'icon-list-loop' : 'icon-song-loop';
        },
        nowPlayTime(state) {
            let nowTime = state.currentTime;
            let m = parseInt(nowTime / 60);
            m = m < 10 ? '0' + m : m;
            let s = parseInt(nowTime % 60);
            s = s < 10 ? '0' + s : s;
            let t = m + ':' + s;
            return t;
        },
        endTime(state) {
            let time = state.totalTime;
            let m = parseInt(time / 60);
            m = m < 10 ? '0' + m : m;
            let s = parseInt(time % 60);
            s = s < 10 ? '0' + s : s;
            time = m + ':' + s;
            return time;
        },
        // 返回当前播放的歌曲
        getCurrentSong(state) {
            return state.playingIndex != null ? state.playlist[state.playingIndex] : null;
        },
        // 返回是否已登录
        loginFlag(state) {
            return state.token != null && state.token != '';
        }
    },
    mutations: {
        // 设置token
        setToken(state, token){
            state.token = token;
        },
        // 设置user
        setUser(state, user){
            state.user = user;
        },
        // 跳转音频到指定位置
        seekAudio(state, position) {
            state.audio.seek(position);
        },
        // 控制播放器页面显示隐藏
        setShowPlayPage(state, show) {
            state.isShowPlayPage = show;
        },
        // 设置播放swiper控件当前item位置id
        setPlaySwiperItemId(state, id) {
            state.playSwiperItemId = id;
        },
        // 播放索引的指定的歌曲
        setPlayingIndex(state, i) {
            if(i == state.playingIndex){
                return;
            }
            state.playingIndex = i;
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        },
        setPlayList(state, content) {
            state.playlist = content.list;
            state.playingIndex = content.i;
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        },
        addAndPlay(state, song) {
            let index = 0;
            if(state.playingIndex != null){
                index = state.playingIndex;
            }
            //去重
            let i = indexOf(state.playlist, song);
            if(state.playlist == null ||state.playlist.length <= 0) {
                index = 0;
            } else if(i == -1){
                index += 1;
            } else if(i == index) {
                return;
            } else if(i < index) {
                state.playlist.splice(i, 1);
            } else {
                state.playlist.splice(i, 1);
                index += 1;
            }
            state.playlist.splice(index, 0, song);
            state.playingIndex = index;
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        },
        async playNewSong(state, song) {
            await songUrl({songId: song.id, musicPlatform: song.musicPlatform}).then(data => {
                song.br = data.br;
                state.audio.src = data.url;
                state.isPlay = true;
            });
            this.commit('playSongGroup');
        },
        addToNext(state, song) {
            let index = 0;
            if(state.playingIndex != null){
                index = state.playingIndex;
            }
            //去重
            let i = indexOf(state.playlist, song);
            if(state.playlist == null ||state.playlist.length <= 0) {
                index = 0;
            } else if(i == -1){
                index += 1;
            } else if(i == index) {
                return;
            } else if(i < index) {
                state.playlist.splice(i, 1);
                state.playingIndex -= 1;
            } else {
                state.playlist.splice(i, 1);
                index += 1;
            }
            state.playlist.splice(index, 0, song);
        },
        removePlayListSong(state, i) {
            state.playlist.splice(i, 1);
            if(state.playlist.length <= 0){
                state.playingIndex = null;
                state.isShowPlaylist = false;
                this.commit('pause');
                return;
            }
            if(i == state.playingIndex) {
                if(state.playMode == 3){
                    this.commit('playNext');
                } else {
                    this.commit('playNewSong', state.playlist[state.playingIndex]);
                }
            } else if(i < state.playingIndex) {
                state.playingIndex = state.playingIndex - 1;
                this.commit('playSongGroup');
            } else {
                this.commit('playSongGroup');
            }
        },
        setTotalTime(state, time) {
            state.totalTime = time;
        },
        setCurrentTime(state, time) {
            state.currentTime = time;
        },
        setShowPlayList(state, show) {
            state.isShowPlaylist = show;
        },
        setVolume(state, volume) {
            var f = parseFloat(volume / 100);  
            if(isNaN(f)){
                return;
            }
            state.volume = volume;
            state.audio.volume = f;
        },
        setPlayMode(state, playMode) {
            state.playMode = playMode;
        },
        // 切换播放模式
        switchPlayMode(state) {
            state.playMode = state.playMode == 1 ? 2 : state.playMode == 2 ? 3 : 1;
            if(state.token != null) {
                userConfig({ playMode: state.playMode}).then(data => {});
            }
        },
        // 切换播放,暂停
        switchPlay(state) {
            state.isPlay = !state.isPlay;
            if(state.isPlay) {
                state.audio.play();
            } else {
                state.audio.pause();
            }
        },
        // 播放
        play(state) {
            state.audio.play();
            state.isPlay = true;
        },
        // 暂停
        pause(state) {
			state.audio.pause();
            state.isPlay = false;
		},
        // 滑动更换歌曲,根据传过来的歌曲信息确定位置
        switchSong(state, song) {
            let i = indexOf(state.playlist, song);
            this.commit('setPlayingIndex', i);
        },
        //前一首
        playPrevious(state) {
            this.commit('switchSong', state.playSongGroup[0]);
        },
        //下一首
        playNext(state) {
            this.commit('switchSong', state.playSongGroup[2]);
        },
        //更新需要的前一曲,当前歌曲,后一曲
        playSongGroup(state) {
            if(state.playlist.length <= 0) {
                state.playSongGroup = [];
            }
            let songs = [];
            if(state.playMode == 1 || state.playlist.length == 1) {
                songs.push(state.playlist[state.playingIndex]);
                songs.push(state.playlist[state.playingIndex]);
                songs.push(state.playlist[state.playingIndex]);
            } else if(state.playMode == 2) {
                songs.push(state.playlist[state.playingIndex - 1 < 0 ? state.playlist.length - 1 : state.playingIndex - 1])
                songs.push(state.playlist[state.playingIndex])
                songs.push(state.playlist[state.playingIndex + 1 >= state.playlist.length ? 0 : state.playingIndex + 1])
            } else {
                let preIndex = randomNum(0, state.playlist.length - 1);
                while(state.playingIndex == preIndex){
                    preIndex = randomNum(0, state.playlist.length - 1);
                }
                songs.push(state.playlist[preIndex]);
                songs.push(state.playlist[state.playingIndex]);
                if(state.playlist.length == 2){
                    songs.push(state.playlist[preIndex]);
                } else {
                    let nextIndex = randomNum(0, state.playlist.length - 1);
                    while (state.playingIndex == nextIndex || preIndex == nextIndex) {
                        nextIndex = randomNum(0, state.playlist.length - 1);
                    }
                    songs.push(state.playlist[nextIndex]);
                }
            }
            state.playSongGroup = songs;
        },
        //更新用户歌单
		updateUserSonglist(state) {
			if(state.token != null && state.token != ''){
                //清空数组
                state.createList = [];
                state.collectList = [];
				userSonglist({type: '0'}).then(data => {
					for (var i=0;i<data.length;i++) { 
						var item = data[i];
						if(item.type == '1') {
							state.favouriteList = item;
						} else if(item.type == '2') {
							state.createList.push(item);
						} else if(item.type == '3'){
							state.collectList.push(item);
						}
					}
				})
			} else {
                state.favouriteList = {}
                state.createList = [];
                state.collectList = [];
            }
		},
        // 我喜欢添加歌曲
        async addToFavourite(state, song) {
            await addSong({songlistId:state.favouriteList.id, songId: song.id});
            this.commit('updateUserSonglist');
        },
        // 我喜欢删除歌曲
        async delToFavourite(state, song) {
            await delSong({songlistId:state.favouriteList.id, songId: song.id});
            this.commit('updateUserSonglist');
        },
        //创建用户歌单
        async addSonglist(state, list) {
            await createSonglist(list);
            this.commit('updateUserSonglist');
        },
        //删除用户歌单
        async delSonglist(state, list) {
            await deleteSonglist(list.id);
            this.commit('updateUserSonglist');
        },
        //歌单添加歌曲
        async addToSonglist(state, data) {
            await addSong({songlistId: data.songlistId, songId: data.songId});
            this.commit('updateUserSonglist');
        },
        //歌单删除歌曲
        async delToSonglist(state, data) {
            await delSong({songlistId: data.songlistId, songId: data.songId});
            this.commit('updateUserSonglist');
        }
    }
})
export default store
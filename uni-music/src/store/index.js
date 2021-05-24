import Vue from 'vue'
import Vuex from 'vuex'
import { songUrl } from '../api/platform'

Vue.use(Vuex);

async function getSongUrl(song) {
    if(song.url == null) {
        await songUrl({songId: song.id, musicPlatform: song.musicPlatform}).then(data => {
            song.url = data.url;
            song.br = data.br;
        });
    }
}

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
        // 音频总时长(音频组件)
		totalTime: null,
        // 音频总时长(显示)
		endTime: '00:00',
        // 当前播放时长(显示)
        playTime: '00:00',
        // 音量
		volume:100,
        // 当前播放时间(音频组件)
		currentTime:0,
        // 播放模式(1单曲,2列表,3随机)
        playMode: 3,
        // 播放器
        audio: audio
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
        }
    },
    mutations: {
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
                index = state.playingIndex + 1;
            }
            //需要去重
            state.playlist.splice(index, 0, song);
            state.playingIndex = index;
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        },
        async playNewSong(state, song) {
            await getSongUrl(song);
            if(song.url != null) {
                state.audio.src = song.url;
                state.isPlay = true;
            }
        },
        addToNext(state, song) {
            let index = 0;
            if(state.playingIndex != null){
                index = state.playingIndex + 1;
            }
            //需要去重
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
            state.volume = volume;
        },
        setPlayMode(state, playMode) {
            state.playMode = playMode;
        },
        // 切换播放模式
        switchPlayMode(state) {
            state.playMode = state.playMode == 1 ? 2 : state.playMode == 2 ? 3 : 1;
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
        // 滑动更换歌曲,因为不知道是左滑还是右滑,需要在这里确定是上一首还是下一首
        switchSong(state, i) {
            if(i < state.playingIndex) {
                this.commit('playPrevious', state);
            } else {
                this.commit('playNext', state);
            }
        },
        //前一首
        playPrevious(state) {
            if(state.playMode == 1){
                state.playingIndex = state.playingIndex;
            } else if(state.playMode == 2) {
                state.playingIndex = state.playingIndex - 1 < 0 ? state.playlist.length - 1 : state.playingIndex - 1;
            } else {
                state.playingIndex = randomNum(0, state.playlist.length - 1)
            }
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        },
        //下一首
        playNext(state) {
            if(state.playMode == 1){
                state.playingIndex = state.playingIndex;
            } else if(state.playMode == 2) {
                state.playingIndex = state.playingIndex + 1 >= state.playlist.length ? 0 : state.playingIndex + 1;
            } else {
                state.playingIndex = randomNum(0, state.playlist.length - 1)
            }
            this.commit('playNewSong', state.playlist[state.playingIndex]);
        }
    }
})
export default store
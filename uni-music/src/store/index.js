import Vue from 'vue'
import Vuex from 'vuex'
import { url, lyric} from '../api/platform'

Vue.use(Vuex);

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
        playMode: 0
    },
    getters: {
        playlistLength(state) {
            return state.playlist.length;
        },
        nextIndex(state) {
            
        },
        preIndex(state) {

        }
    },
    mutations: {
        setPlayingIndex(state, i) {
            state.playingIndex = i;
        },
        setPlayList(state, list, i) {
            state.playlist = list;
            state.playingIndex = i;
        },
        addAndPlay(state, song) {
            if(song.url == null) {
                url({songId: song.id, musicPlatform: song.musicPlatform}).then(data => {
                    song.url = data.url;
                    song.br = data.br;
                });
            }
            let index = 0;
            if(state.playingIndex != null){
                index = state.playingIndex + 1;
            }
            //需要去重
            state.playlist.splice(index, 0, song);
            state.playingIndex = index;
        },
        addToNext(state, song) {
            if(song.url == null) {
                url({songId: song.id, musicPlatform: song.musicPlatform}).then(data => {
                    song.url = data.url;
                    song.br = data.br;
                });
            }
            let index = 0;
            if(state.playingIndex != null){
                index = state.playingIndex + 1;
            }
            //需要去重
            state.playlist.splice(index, 0, song);
        },
        removePlayList(state, i) {
            state.playlist.splice(i, 1);
        },
        setTotalTime(state, time) {
            state.totalTime = time;
        },
        setCurrentTime(state, time) {
            state.currentTime = time;
        },
        setPlay(state, play) {
            state.isPlay = play;
        },
        setShowPlayList(state, show) {
            state.isShowPlaylist = show;
        },
        setVolume(state, volume) {
            state.volume = volume;
        },
        setPlayMode(state, playMode) {
            state.playMode = playMode;
        }
    }
})
export default store
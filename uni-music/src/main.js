import Vue from 'vue'
import App from './App'
import store from './store/index.js';


//使用uView
import uView from "uview-ui";
Vue.use(uView);

Vue.config.productionTip = false
Vue.prototype.$store = store

App.mpType = 'app'

// 挂载一个可全局访问的音频组件
// let audio = uni.createInnerAudioContext();

// audio.autoplay = true;
// // 音频就绪后触发的回调函数
// audio.onCanplay(() => {
// 	let time = audio.duration;
// 	store.commit('setTotalTime', time);
// 	let m = parseInt(time / 60);
// 	m = m < 10 ? '0' + m : m;
// 	let s = parseInt(time % 60);
// 	s = s < 10 ? '0' + s : s;
// 	time = m + ':' + s;
// 	// store.commit('getEndTime', time);
// })
// // 音频进度更新后触发的时间
// audio.onTimeUpdate(() => {
// 	let nowTime = audio.currentTime;
// 	store.commit('setCurrentTime', nowTime);
// 	let m = parseInt(nowTime / 60);
// 	m = m < 10 ? '0' + m : m;
// 	let s = parseInt(nowTime % 60);
// 	s = s < 10 ? '0' + s : s;
// 	let t = m + ':' + s;
// 	// store.commit('getnowPlayTime', t);
// })
// // 音频自然播放结束后触发的事件
// audio.onEnded(() => {
// 	store.commit('playNext');
// })
// Vue.prototype.$audio = audio;

const app = new Vue({
  store,
  ...App
})
app.$mount()

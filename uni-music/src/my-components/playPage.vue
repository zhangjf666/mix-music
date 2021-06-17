<template>
    <view class="playPage">
        <u-popup v-model="isShow" mode="bottom" height="100%" :custom-style="style">
            <!-- 歌曲信息 -->
            <view class="pageHead">
                <!-- #ifndef MP-WEIXIN -->
                <!-- <text class="iconfont icon-left" style="margin: 0 26rpx;color: #F1F1F1;font-size: 60rpx;"></text> -->
                <u-icon name="arrow-down" style="position: fixed;margin: 0 26rpx;color: #F1F1F1;font-size: 50rpx;" @click="setShowPlayPage(false)"></u-icon>
                <!-- #endif -->
                <view class="info">
                    <view class="name">{{ playlist[playingIndex].name }}</view>
                    <view class="singer">{{ songSinger(playlist[playingIndex]) }}</view>
                </view>
            </view>
            <!--歌词及唱片机 -->
            <view class="pageMain" @click.self="switchLyric()">
                <view class="recordPlayer" v-if="!isLyrics">
                    <swiper
                        ref="swiper"
                        class="pageSwiper"
                        :duration="duration"
                        :circular="circular"
						:currentItemId="playSwiperItemId"
                        @change="getCurrent"
                        @transition="pageTransition"
                        @animationfinish="pageAnimationfinish"
                    >
                        <swiper-item item-id="pre" @click="switchLyric()" v-if="songPre">
                            <view class="imgBox" :class="[isPlay && isSpin ? 'spin' : 'noSpin']">
                                <image style="width: 540rpx;height: 540rpx;" src="../static/image/play_disc.png"></image>
                                <image class="songImg" style="width: 320rpx;height: 320rpx;border-radius: 50%;" :src="songPre.picUrl"></image>
                            </view>
                        </swiper-item>
                        <swiper-item item-id="current" @click="switchLyric()" v-if="songCurrent">
                            <view class="imgBox" :class="[isPlay && isSpin ? 'spin' : 'noSpin']">
                                <image style="width: 540rpx;height: 540rpx;" src="../static/image/play_disc.png"></image>
                                <image class="songImg" style="width: 320rpx;height: 320rpx;border-radius: 50%;" :src="songCurrent.picUrl"></image>
                            </view>
                        </swiper-item>
                        <swiper-item item-id="next" @click="switchLyric()" v-if="songNext">
                            <view class="imgBox" :class="[isPlay && isSpin ? 'spin' : 'noSpin']">
                                <image style="width: 540rpx;height: 540rpx;" src="../static/image/play_disc.png"></image>
                                <image class="songImg" style="width: 320rpx;height: 320rpx;border-radius: 50%;" :src="songNext.picUrl"></image>
                            </view>
                        </swiper-item>
                    </swiper>
                    <image class="pointer" :class="isPlay && isSpin ? 'angle' : ''" src="../static/image/play_needle.png"></image>
                </view>
                <!-- 歌词 -->
                <view class="lyricsBox" v-else>
                    <!-- 音量调节 -->
                    <view class="volume">
                        <text class="iconfont icon-volume"></text>
                        <u-slider
                            class="slider"
                            v-model="volumes"
                            block-width="20"
                            step="1"
                            height="4"
                            active-color="#f1f1f1"
                            inactive-color="rgba(255,255,255,.5)"
                            @moving="setVolume"
                        ></u-slider>
                    </view>
                    <!-- 歌词滚动区 -->
                    <view class="lyrics" @click="switchLyric()">
                        <bing-lyric :lyrics="lyrics" :curTime="currentTime" :lyricStyle="lyricStyle" :areaStyle="areaStyle" :centerStyle="centerStyle" @centerBtnClick="seekTime"></bing-lyric>
                    </view>
                </view>
            </view>
            <!-- 进度条及功能区 -->
            <view class="pageFeatures">
                <!-- 播放进度条 -->
                <view class="progress-bar">
                    <text>{{ nowPlayTime }}</text>
                    <u-slider
                            class="progress-slider"
                            v-model="activeWidth"
                            block-width="20"
                            height="4"
                            active-color="#f1f1f1"
							:max="totalTime"
                            inactive-color="rgba(255,255,255,.5)"
                            @end="skipProgress()"
                        ></u-slider>
                    <text>{{ endTime }}</text>
                </view>
                <!-- 按钮功能区 -->
                <view class="play-btn">
                    <text class="iconfont" :class="inFavourite ? 'icon-like1':'icon-like'" @click="changeFavourite()"></text>
                    <text class="iconfont" :class="playModeIcon" @click="clickSwitchPlayMode()"></text>
                    <text class="iconfont icon-top-music" @click="previousMusic"></text>
                    <!-- 播放与暂停 -->
                    <view class="playPause" @click="switchPlay()">
                        <text class="iconfont icon-pause" v-if="isPlay"></text>
                        <text class="iconfont icon-play" v-else></text>
                    </view>
                    <text class="iconfont icon-next-music" @click="nextMusic"></text>
                    <text class="iconfont icon-playList" @click="setShowPlayList(true)"></text>
                    <view class="comments" @click="goCommentPage">
                        <text class="iconfont icon-comment"></text>
                        <text class="total">{{ total }}</text>
                    </view>
                </view>
            </view>
            <!-- 播放列表 -->
            <play-list></play-list>
            <!-- 背景 -->
            <view class="bg" :style="isBg"></view>
            <u-toast ref="uToast" />
        </u-popup>
    </view>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex';
import playList from './playList.vue';
import { songLyric } from '@/api/platform.js';
import { existSong } from '@/api/songlist.js';
import { handleSingerName } from '../utils/songUtil.js'

export default {
    name: 'playPage',
    components: {
		playList
	},
    data() {
        return {
            // 动画时间
			duration: 0,
			// 是否循环swiper组件,通过绑定currentItemId值切换动画有bug,需要动态切换是否循环来处理
			circular: true,
            // 判断动画是否暂停
			isSpin: true,
			// 播放进度条宽度
			offsetWidth: 100,
			// 每单位的宽度
			unitWidth: 0,
			// 当前进度条长度
			activeWidth: 0,
			// 显示歌词
			isLyrics: false,
			// 歌词
			lyrics: [],
			// 歌词的颜色
			lyricStyle: {
				color: "#ADADAD",
				fontSize: "14px",
				activeFontSize: "16px"
			},
			// 歌词背景设置
			areaStyle: {
				background: '0'
			},
			centerStyle: {
				btnImg: '../../static/image/btn.png'
			},
			// 总评论数
			total: '',
            // popup样式
			style: {
				width: '100%',
				margin: '0 auto 0rpx'
			},
            songPre: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
			songCurrent: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
			songNext: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
			//当前歌曲是否在我喜欢列表中
			inFavourite: false
        }
    },
	mounted() {
		if(this.playingIndex !== null){
			this.updateSongs();
		}
	},
    methods: {
		...mapMutations(['playPrevious', 'seekAudio','playNext', 'switchPlay', 'switchSong','setPlaySwiperItemId','setPlayingIndex','setShowPlayList', 'setShowPlayPage','setPlayMode', 'removePlayListSong','switchPlayMode','setVolume','addToFavourite','delToFavourite']),
		// current值发生改变时触发的事件
		getCurrent(e) {
			if (e.detail.source === 'touch') {
				this.setPlaySwiperItemId(e.detail.currentItemId)
				this.switchSong(e.detail.currentItemId == 'pre' ? this.songPre : e.detail.currentItemId == 'current' ? this.songCurrent : this.songNext);
			}
		},
        // swiper-item位置发生变化时触发的事件
		pageTransition() {
			this.duration = 500;
			this.isSpin = false;
		},
		// 动画结束后触发的事件
		pageAnimationfinish() {
			this.duration = 0;
			this.isSpin = true;
			this.circular = true;
		},
		//切换歌词
		async switchLyric(){
			if(this.isLyrics == true) {
				this.isLyrics = false;
				return;
			}
			// 获取歌词
			let song = this.playSwiperItemId == 'pre' ? this.songPre : this.playSwiperItemId == 'current' ? this.songCurrent : this.songNext;
			if(song.lyric == null || song.lyric != ''){
				await songLyric({ songId: song.id, musicPlatform: song.musicPlatform }).then(data => {
					song.lyric = data;
				});
			}
			this.lyrics = song.lyric.replace(/\[/g, 'sb[').split('sb');
			this.isLyrics = true;
		},
        // 跳转评论页面
        goCommentPage() {

        },
		// 跳转到歌词时间播放
		seekTime(e) {
			this.seekAudio(e.centerTime);
		},
		// 播放进度条跳转
		skipProgress() {
			this.seekAudio(this.activeWidth / this.unitWidth);
		},
        // 上一曲
        previousMusic() {
			if(this.playSwiperItemId == 'next'){
				this.circular = false;
			}
			this.duration = 1000;
			this.setPlaySwiperItemId(this.playSwiperItemId == 'pre' ? 'next' : this.playSwiperItemId == 'next' ? 'current' : 'pre');
			this.playPrevious();
        },
        // 下一曲
        nextMusic() {
			this.duration = 1000;
            this.setPlaySwiperItemId(this.playSwiperItemId == 'pre' ? 'current' : this.playSwiperItemId == 'next' ? 'pre' : 'next');
			this.playNext();
		},
		// 切换后更新歌曲
		updateSongs() {
			if(this.playSongGroup.length <= 0){
				this.songPre = this.defaultSong;
				this.songCurrent = this.defaultSong;
				this.songNext = this.defaultSong;
			} else if(this.playSwiperItemId == 'pre'){
				this.songPre = this.playSongGroup[1];
				this.songCurrent = this.playSongGroup[2];
				this.songNext = this.playSongGroup[0];
			} else if(this.playSwiperItemId == 'current') {
				this.songPre = this.playSongGroup[0];
				this.songCurrent = this.playSongGroup[1];
				this.songNext = this.playSongGroup[2];
			} else if(this.playSwiperItemId == 'next') {
				this.songPre = this.playSongGroup[2];
				this.songCurrent = this.playSongGroup[0];
				this.songNext = this.playSongGroup[1];
			}
			// 获取歌词
			if(this.isLyrics) {
				this.lyrics = [];
				let song = this.playSwiperItemId == 'pre' ? this.songPre : this.playSwiperItemId == 'current' ? this.songCurrent : this.songNext;
				if(song.lyric == null || song.lyric	!= ''){
					songLyric({ songId: song.id, musicPlatform: song.musicPlatform }).then(data => {
						song.lyric = data;
						this.lyrics = song.lyric.replace(/\[/g, 'sb[').split('sb');	
					});
				} else {
					this.lyrics = song.lyric.replace(/\[/g, 'sb[').split('sb');	
				}
			}
			//更新我喜欢标识
			this.isFavourite();
		},
		//切换播放模式
		clickSwitchPlayMode() {
			this.switchPlayMode();
			this.$refs.uToast.show({
                title: this.playModeText,
                duration: 1000,
                position: 'bottom'
            })
		},
		//保存或者删除到我喜欢
		changeFavourite() {
			if(!this.loginFlag) {
				this.$refs.uToast.show({
					title: '请先登录',
					duration: 1000,
					position: 'bottom'
				})
				return;
			}
			if(this.inFavourite){
				this.delToFavourite(this.getCurrentSong);
				this.inFavourite = false;
			} else {
				this.addToFavourite(this.getCurrentSong);
				this.inFavourite = true;
			}
		},
		// 当前歌曲是否是我喜欢中的歌曲
        isFavourite() {
            if(this.getCurrentSong == null) {
                this.inFavourite = false;
            }
            existSong({songlistId: this.favouriteList.id, songId: this.getCurrentSong.id}).then(data => {
				this.inFavourite = data;
            })
        }
	},
	computed: {
        ...mapGetters(['loginFlag','playlistLength','playModeIcon','playModeText','nowPlayTime','endTime','getCurrentSong']),
		...mapState(['favouriteList','volume','currentTime', 'totalTime','isPlay', 'isShowPlayList', 'isShowPlayPage', 'playlist', 'playingIndex','playSongGroup','playMode','playSwiperItemId']),
        isShow: {
            get() {
                return this.isShowPlayPage;
            },
            set(val) {
                this.setShowPlayPage(val);
            }
        },
		// 背景
        isBg() {
			return `background: url(${this.playlist[this.playingIndex].picUrl}) center;`;
		},
		// 音量
		volumes: {
			set(val) {
				this.setVolume(val);
			},
			get() {
				return this.volume;
			}
		},
		// 处理歌手名字
		songSinger() {
			return (song) => {
                return handleSingerName(song);
            }
		},
		favouriteClass() {
			return this.isFavourite ? 'icon-like1':'icon-like';
		}
	},
    watch: {
		playSongGroup() {
			this.updateSongs();
		},
		currentTime() {
			this.activeWidth = this.currentTime * this.unitWidth;
		},
		totalTime() {
			this.unitWidth = this.offsetWidth / this.totalTime;
		}
    }
}
</script>

<style lang="scss" scoped>
.bg {
	position: fixed;
	top: 0px;
	left: 0px;
	height: 110%;
	width: 110%;
	margin: -5%;
	background-size: cover;
	-webkit-filter: blur(10px) brightness(0.6);
	-moz-filter: blur(10px) brightness(0.6);
	filter: progid:DXImageTransform.Microsoft.Blur(PixelRadius=10, MakeShadow=false); /* IE6~IE9 */
	z-index: -1;
}
.playPage{
	height: 100%;
}
.pageHead {
	flex: 1;
	display: flex;
	height: calc(11vh - var(--window-top));
	align-items: center;
	.info {
		width: 100%;
		/* #ifdef MP-WEIXIN */
		margin: 0 auto;
		text-align: center;
		/* #endif */
		.name {
			font-size: 32rpx;
			color: #f1f1f1;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
            text-align: center;
		}
		.singer {
			font-size: 20rpx;
			color: #dfdfdf;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
            text-align: center;
		}
	}
}
.pageMain {
	// margin: 0 auto;
	flex: 1;
	height: calc(70.5vh - var(--window-top));
	/* #ifdef MP-WEIXIN */
	height: calc(70.5vh - var(--window-top));
	/* #endif */
	overflow: hidden;
	.pageSwiper {
		margin: 200rpx auto 0;
		height: 540rpx;
	}
	.imgBox {
		position: relative;
		margin: 0 auto;
		width: 540rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		.songImg {
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
		}
	}
	.pointer {
		position: absolute;
		top: 180rpx;
		left: 44%;
		width: 210rpx;
		height: 330rpx;
		transform: rotate(-30deg);
		transform-origin: 20px 20px;
		transition: transform 0.8s;
	}
	.volume {
		display: flex;
		height: 40rpx;
		align-items: center;
		.icon-volume {
			color: #ccc;
			margin: 0 30rpx 0 50rpx;
			font-size: 36rpx;
		}
		.slider {
			width: 560rpx;
		}
	}
}
.pageFeatures {
	height: calc(18.5vh - var(--window-top));
	/* #ifdef MP-WEIXIN */
	height: calc(18.5vh - var(--window-top));
	/* #endif */
	color: #f1f1f1;
	padding-top: 60rpx;
	/* #ifdef MP-WEIXIN */
	padding-top: 10rpx;
	/* #endif */
	.play-btn {
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin-top: 40rpx;
		text {
			font-size: 40rpx;
		}
		.playPause {
			text {
				font-size: 80rpx;
			}
		}
		.comments{
			position: relative;
			width: 80rpx;
			.total{
				position: absolute;
				display: inline-block;
				width: 64rpx;
				height: 34rpx;
				// background-color:#7F7D7C;
				font-size: 24rpx;
				right: -8rpx;
				top: -10rpx;
			}
		}
		.icon-like1 {
			color: #FA3534;
			font-size: 44rpx;
		}
	}
	.progress-bar {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 40rpx;
		text {
			font-size: 24rpx;
		}
		.linBox {
			display: flex;
			position: relative;
			margin: 0 30rpx;
		}
		.line {
			width: 500rpx;
			height: 1px;
			background-color: rgba($color: #fff, $alpha: 0.6);
		}
		.activityLine {
			position: absolute;
			left: 0;
			.activityLineItem {
				position: relative;
				height: 1px;
				background-color: #fff;
				.icon-dots {
					position: absolute;
					right: -32rpx;
					top: -40rpx;
					font-size: 60rpx;
				}
			}
		}
		.progress-slider {
			width: 500rpx;
			margin: 0 26rpx;
		}
	}
}

/* 动画 */
@keyframes rotate-disk {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}
@keyframes rotate-disk1 {
	100% {
		transform: rotate(0deg);
	}
}
.spin {
	animation: rotate-disk 20s infinite normal linear;
	animation-play-state: play;
}
.noSpin {
	animation: rotate-disk 20s infinite normal linear;
	animation-play-state: paused;
}
.rotate {
	animation: rotate-disk1 0s infinite normal linear;
}
.pageMain .angle {
	transform: rotateZ(-2deg);
}
</style>
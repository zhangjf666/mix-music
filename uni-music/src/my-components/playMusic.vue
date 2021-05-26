<template>
	<view class="playMusic" v-if="isNull">
		<swiper
			ref="swiper"
			class="playSwiper"
			circular
			:duration="duration"
			:currentItemId="playSwiperItemId"
			@change="getCurrent"
			@transition="playTransition"
			@animationfinish="playAnimationfinish"
		>
			<swiper-item item-id="pre" @click="setShowPlayPage(true)">
				<u-image class="songImg" :src="songPre.picUrl" mode="widthFix" width="80" height="80" shape="circle"></u-image>
				<view class="songInfo">
					<view>
						<text class="songName">{{ songPre.name }}</text>
						<text class="horizontal">-</text>
						<text class="singerName">{{ songPre.singerName }}</text>
					</view>
					<view class="lyrics">横滑可以切换上下首哦</view>
				</view>
			</swiper-item>
			<swiper-item item-id="current" @click="setShowPlayPage(true)">
				<u-image class="songImg" :src="songCurrent.picUrl" mode="widthFix" width="80" height="80" shape="circle"></u-image>
				<view class="songInfo">
					<view>
						<text class="songName">{{ songCurrent.name }}</text>
						<text class="horizontal">-</text>
						<text class="singerName">{{ songCurrent.singerName }}</text>
					</view>
					<view class="lyrics">横滑可以切换上下首哦</view>
				</view>
			</swiper-item>
			<swiper-item item-id="next" @click="setShowPlayPage(true)">
				<u-image class="songImg" :src="songNext.picUrl" mode="widthFix" width="80" height="80" shape="circle"></u-image>
				<view class="songInfo">
					<view>
						<text class="songName">{{ songNext.name }}</text>
						<text class="horizontal">-</text>
						<text class="singerName">{{ songNext.singerName }}</text>
					</view>
					<view class="lyrics">横滑可以切换上下首哦</view>
				</view>
			</swiper-item>
		</swiper>
		<view class="btn">
			<view class="btn-item" @click="switchPlay()">
				<u-icon class="btnItem" name="pause-circle" v-if="isPlay"></u-icon>
				<u-icon class="btnItem" name="play-circle" v-else></u-icon>
			</view>
			<u-icon class="btnItem" name="list-dot" @tap="setShowPlayList(true)"></u-icon>
		</view>

		<!-- 播放器 -->
		<play-page></play-page>
		<!-- 播放列表 -->
		<play-list></play-list>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import playList from './playList.vue';
import playPage from './playPage.vue';

export default {
	name: 'playMusic',
	data() {
		return {
			// 动画时间
			duration: 0,
			// popup样式
			style: {
				width: '90%',
				margin: '0 auto 36rpx',
				borderRadius: '20rpx'
			},
			//歌曲列表背景控制
			songBg: null,
			songPre: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
			songCurrent: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
			songNext: {name:'没有歌曲了', picUrl:'../static/image/play_disc.png', singerName:'无'},
		};
	},
	components:{
		playList,
		playPage
	},
	methods: {
		// 控制播放
		...mapMutations(['switchPlay','setShowPlayList','switchSong', 'setPlaySwiperItemId', 'setShowPlayPage']),
		// current值发生改变时触发的事件
		getCurrent(e) {
			if (e.detail.source === 'touch') {
				this.setPlaySwiperItemId(e.detail.currentItemId)
				this.switchSong(e.detail.currentItemId == 'pre' ? this.songPre : e.detail.currentItemId == 'current' ? this.songCurrent : this.songNext);
			}
		},
		// swiper-item位置发生变化时触发的事件
		playTransition() {
			this.duration = 500;
		},
		// 动画结束后触发的事件
		playAnimationfinish() {
			this.duration = 0;
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
		}
	},
	computed: {
		...mapState(['isPlay', 'playlist', 'playingIndex','playSongGroup','playMode','playSwiperItemId']),
		// 处理微信兼容v-if问题
		isNull() {
			return this.playingIndex !== null;
		}
	},
	watch: {
		playSongGroup() {
			this.updateSongs();
		}
    }
};
</script>

<style lang="scss" scoped>
.playMusic {
	position: fixed;
	display: flex;
	justify-content: space-around;
	align-items: center;
	bottom: 0;
	width: 100%;
	height: 100rpx;
	// padding: 0 10rpx;
	box-shadow: 0px -2px 10px rgba(0, 0, 0, 0.2);
	border-top: 1px solid #eee;
	background-color: rgba(255, 255, 255, 0.9);
	// z-index: 999999;
	.playSwiper {
		height: 100%;
		width: 75%;
		swiper-item {
			display: flex;
			align-items: center;
		}
		.songImg {
			margin: 0 15rpx;
		}
		.songInfo {
			// width: 55%;
			// font-size: 24rpx;
			.songName {
				font-size: 24rpx;
				color: #000;
				width: 440rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
			.horizontal {
				margin: 0 6rpx;
				color: #666;
			}
			.singerName {
				color: #666;
				font-size: 20rpx;
				white-space: nowrap;
				text-overflow: ellipsis;
			}
			.lyrics {
				color: #666;
                font-size: 20rpx;
			}
		}
	}
	.btn {
		width: 24%;
		font-size: 56rpx;
		margin-right: 0rpx;
		display: flex;
		justify-content: space-around;
	}
}
</style>

<template>
	<view class="playMusic" v-if="isNull">
		<swiper
			class="playSwiper"
			circular
			:duration="duration"
			:current="playingIndex"
			@change="getCurrent"
			@transition="playTransition"
			@animationfinish="playAnimationfinish"
		>
			<swiper-item v-for="(item, i) in playlist" :key="item.id" @click="goPlayPage(i)">
				<u-image class="songImg" :src="item.picUrl" mode="widthFix" width="80" height="80" shape="circle"></u-image>
				<view class="songInfo">
					<view class="songName">{{ item.name }}</view>
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

		<!-- 播放列表 -->
		<play-list></play-list>
	</view>
</template>

<script>
import { mapState, mapMutations } from 'vuex';
import playList from './playList.vue';

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
			songBg: null
		};
	},
	components:{
		playList
	},
	methods: {
		// 控制播放
		...mapMutations(['switchPlay','setShowPlayList','switchSong']),
		// current值发生改变时触发的事件
		getCurrent(e) {
			if (e.detail.source === 'touch') {
				this.switchSong(e.detail.current);
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
		// 跳转播放页面
		goPlayPage(i) {
			uni.navigateTo({
				url: '/pages/play/play'
			});
		}
	},
	computed: {
		...mapState(['isPlay', 'playlist', 'playingIndex']),
		// 处理微信兼容v-if问题
		isNull() {
			return this.playingIndex !== null;
		}
	},
	watch: {
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
			font-size: 24rpx;
			.songName {
				color: #000;
				width: 440rpx;
				overflow: hidden;
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

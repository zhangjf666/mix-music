<template name="Mine">
	<view class="mine">
		<!-- user info -->
		<view class="mine-user">
			<view class="user-info">
				<view class="user-ico"><text class="user-icon text-black iconfont icon-yonghu"></text></view>
				<view class="user-msg" v-if="loginFlag">{{userName}}</view>
				<view class="user-msg" v-else>登录立享手机电脑多端同步</view>
			</view>
			<view class="user-login" v-if="!loginFlag" style="font-size: 24rpx;padding: 6rpx;" @click="goLoginPage()">立即登录</view>
		</view>
		<!-- 我喜欢 -->
		<view class="card">
			<view class="card-item">
				<view class="item-cover">
					<text class="iconfont icon-like1"></text>
				</view>
				<view class="item-info">
					<view class="info-name">我喜欢的音乐</view>
					<view class="info-songcount" v-if="favouriteList.songCount">{{ favouriteList.songCount }}首</view>
				</view>
			</view>
		</view>
		<!-- 创建的歌单 -->
		<view class="card">
			<view class="card-item">
				<view>创建的歌单: {{ createList.length }}个</view>
			</view>
			<view class="card-item" v-for="(item, i) in createList" :key="i">
				<view class="item-cover">
					<text class="iconfont icon-like1"></text>
				</view>
				<view class="item-info">
					<view class="info-name">{{item.name}}</view>
					<view class="info-songcount">{{item.songCount}}首</view>
				</view>
			</view>
		</view>
		<!-- 收藏的歌单 -->
		<view class="card">
			<view class="card-item">
				<view>收藏的歌单: {{ collectList.length }}个</view>
			</view>
			<view class="card-item" v-for="(item, i) in collectList" :key="i">
				<view class="item-cover">
					<text class="iconfont icon-like1"></text>
				</view>
				<view class="item-info">
					<view class="info-name">{{item.name}}</view>
					<view class="info-songcount">{{item.songCount}}首</view>
				</view>
			</view>
		</view>

		<!-- 最近播放 -->
		<view class="user-new-title">
			<view class="new-title">最近播放</view>
			<text class="new-title-icon text-black"></text>
		</view>
		
		<!-- last -->
		<view class="last">到底啦&nbsp;~</view>
	</view>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex';

export default {
	name: 'Mine',
	props: {},
	data() {
		return {
		};
	},
	methods: {
		//到登录页面
		goLoginPage() {
			uni.navigateTo({
				url: '../login/login',
			});
		}
	},
	computed: {
		...mapState(['user', 'token','favouriteList','createList']),
		...mapGetters(['loginFlag']),
		userName() {
			return this.user.nickName;
		}
	}
};
</script>

<style lang="scss" scoped>
.mine{
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
	align-items: center;
	background-color: rgb(243, 243, 243);
}
.card {
	width: calc(100% - 48rpx);
	margin: 24rpx;
	display: flex;
	flex-direction: column;
	background-color: white;
	border-radius: 12rpx;
	.card-item {
		margin: 20rpx 20rpx 20rpx 20rpx;
		display: flex;
		flex-direction: row;
		align-items: center;
		.item-cover{
			width: 100rpx;
			height: 100rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			background-color: #ababab;
			border-radius: 10px;
		}
		.item-info {
			margin-left: 20rpx;
			.info-name {
				font-size: 30rpx;
			}
			.info-songcount {
				font-size: 20rpx;
				color: #929292;
			}
		}
	}
}
.mine .mine-user{
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: 60rpx;
	margin-bottom: 30rpx;
}
.mine-user .user-info{
	display: flex;
	align-items: center;
	justify-content: center;
}
.mine-user .user-ico{
	margin-left: 45upx;
	width: 100upx;
	height: 100upx;
	border-radius: 50%;
	background-color: #c0c0c0;
	text-align: center;
	line-height: 110upx;
}
.user-info .user-icon{
	font-size: 36px;
	color: #666;
}
.mine-user .user-msg{
	margin-left: 25upx;
	font-size: 12px;
}
.mine-user .user-login{
	
	margin: 35upx;
	border: 1px solid #c0c0c0;
	border-radius: 10px;
}
.user-new-title{
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: 45upx;
}
.user-new-title .new-title{
	margin-left: 25upx;
	font-size: 16px;
}
.user-new-title .new-title-icon{
	margin-right: 25upx;
	font-size: 24px;
}
.icon-like1{
		color: #FA3534;
}
.last{
	width: 100%;
	height: 80upx;
	margin-top: 55upx;
	text-align: center;
	font-size: 12px;
	color: #ababab;
}
</style>

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
				<view class="item-info" @click="goUserSonglistDetails(favouriteList.id, favouriteList.type)">
					<view class="info-name">我喜欢的音乐</view>
					<view class="info-songcount" v-if="favouriteList.id != null">{{ favouriteList.songCount }}首</view>
				</view>
			</view>
		</view>
		<!-- 创建的歌单 -->
		<view class="card">
			<view class="card-item">
				<view class="item-title">创建的歌单: ({{ createList.length }}个)</view>
				<text class="iconfont icon-add" v-if="loginFlag" @click="showCreate"></text>
			</view>
			<view class="card-item" v-for="(item, i) in createList" :key="i">
				<view class="item-cover">
					<u-image class="item-image" v-if="item.picUrl" :src="item.picUrl" mode="widthFix" width="100rpx" height="100rpx" border-radius="7px"></u-image>
				</view>
				<view class="item-info" @click="goUserSonglistDetails(item.id, item.type)">
					<view class="info-name">{{item.listName}}</view>
					<view class="info-songcount">{{item.songCount}}首</view>
				</view>
				<view class="item-menu" @click="openMenu(item)">
					<text class="iconfont icon-gengduo"></text>
				</view>
			</view>
		</view>
		<!-- 收藏的歌单 -->
		<view class="card">
			<view class="card-item">
				<view class="item-title">收藏的歌单: ({{ collectList.length }}个)</view>
			</view>
			<view class="card-item" v-for="(item, i) in collectList" :key="i">
				<view class="item-cover">
					<text class="iconfont icon-like1"></text>
				</view>
				<view class="item-info" @click="goUserSonglistDetails(item.id, item.type)">
					<view class="info-name">{{item.listName}}</view>
					<view class="info-songcount">{{item.songCount}}首</view>
				</view>
				<view class="item-menu" @click="openMenu(item)">
					<text class="iconfont icon-gengduo"></text>
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

		<!-- 歌单菜单 -->
		<u-popup class="pop-menu" v-model="menuShow" mode="bottom" border-radius="24">
			<view class="pop-menu-title">歌单: {{ currentList.listName }}</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" v-if="currentList.type == '2'">
				<text class="iconfont icon-order"></text>
				<text style="margin-left: 20rpx">编辑歌单信息</text>
			</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doDeleteSonglist">
				<text class="iconfont icon-delete"></text>
				<text style="margin-left: 20rpx">删除</text>
			</view>
		</u-popup>

		<!-- 添加歌单名称 -->
		<u-modal v-model="addListShow" title="新建歌单" show-cancel-button @confirm="doCreate">
			<view style="margin: 10rpx 0 20rpx 20rpx;">歌单名称:</view>
			<view style="margin: 10rpx 0 20rpx 20rpx;">
				<input :value="listName" placeholder="请输入歌单名称" @input="inputChange"/>
			</view>
		</u-modal>
		<play-music></play-music>
	</view>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex';
import playMusic from '@/my-components/playMusic.vue';

export default {
	name: 'Mine',
	components: {
        playMusic
    },
	data() {
		return {
			//添加歌单名称内容
			listName: 'ces',
			//添加歌单对话框是否显示
			addListShow: false,
			//歌单菜单是否显示
			menuShow: false,
			//当前菜单选中的歌单
			currentList: {}
		};
	},
	methods: {
		...mapMutations(['addSonglist','delSonglist']),
		//到登录页面
		goLoginPage() {
			uni.navigateTo({
				url: '../login/login',
			});
		},
		//打开菜单
		openMenu(item) {
			this.menuShow = true;
			this.currentList = item;
		},
		//删除歌单
		doDeleteSonglist() {
			this.delSonglist(this.currentList);
			this.menuShow = false;
		},
		//创建歌单点击
		showCreate() {
			this.listName = '';
			this.addListShow = true;
		},
		//创建歌单
		doCreate() {
			if(this.listName == ''){
				return;
			}
			let list = {};
			list['listName'] = this.listName;
			list['type'] = '2';
			this.addSonglist(list);
		},
		//输入变化
		inputChange(e) {
			this.listName = e.detail.value
		},
		// 去歌单详情页
		goUserSonglistDetails(id, type){
			uni.navigateTo({
				url:`../mylist/mylist?id=${id}&type=${type}`
			})
		}
	},
	computed: {
		...mapState(['user', 'token','favouriteList','createList','collectList']),
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
		.item-title {
			font-size: 20rpx;
			color: #929292;
			width: 100%;
		}
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
		.item-menu {
			margin-left: auto;
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
.pop-menu {
	font-size: 32rpx;
	.pop-menu-title {
		font-size: 26rpx;
		margin: 30rpx 30rpx 0rpx 30rpx;
		padding-bottom: 20rpx;
		border-bottom: #c0c0c0 solid 1px;
	}
	.pop-menu-item {
		height: 100rpx;
		padding: 30rpx 30rpx 30rpx 30rpx;
		// margin: 30rpx 30rpx 30rpx 30rpx;
	}
}
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>

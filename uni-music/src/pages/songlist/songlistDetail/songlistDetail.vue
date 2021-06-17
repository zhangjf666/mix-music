<template>
	<view>
		<u-navbar
			class="navbar"
			:background="{ backgroundColor: '#D83D34' }"
			back-icon-color="#fff"
			back-icon-size="38"
			back-text="歌单"
            z-index="0"
			:back-text-style="{ color: '#fff', fontSize: '34rpx', marginLeft: '10rpx' }"
		></u-navbar>
		<view v-if="songlistDetail" class="scroll">
			<!-- 歌单信息 -->
			<view class="playListInfo" v-if="songlistDetail.playList">
				<view class="imgBox">
					<image :src="songlistDetail.playList.picUrl" class="img" mode="widthFix"></image>
					<view class="playNumber">
						<text
							class="iconfont icon-bofang"
							style="font-size: 24rpx"
						></text>
						{{ isPlayCount(songlistDetail.playList.playCount) }}
					</view>
				</view>
				<view class="playListSummary">
					<view class="listName">{{ songlistDetail.playList.name }}</view>
					<view class="listDesc">{{ songlistDetail.playList.summary }}</view>
				</view>
			</view>
			<!-- 播放全部吸顶 -->
			<u-sticky offset-top="0">
				<view class="playall" hover-class="click-bg" hover-stay-time="200" @click="playSongList(0)">
					<text class="iconfont icon-playall playicon"></text>
					<text class="playtext">播放全部</text>
					<text class="iconfont collect-icon" :class="isCollect ? 'icon-chenggong':'icon-shoucang'" @click.stop="doCollectSonglist"></text>
				</view>
			</u-sticky>
			<!-- 歌单列表 -->
			<scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
				<view class="song-list">
					<view class="song-list-item" v-for="(item, index) in songs" :key="item.id" @click="addAndPlay(item)"
					@touchstart="newTouchstart(index)" @touchend="songBg = null" :style="index === songBg ? 'background-color:rgba(0,0,0,.1)' : ''"
					>
						<view class="song-list-info">
							<text class="iconfont icon-ranking song-serial-icon" v-if="inPlayList(item)"></text>
								<text class="song-serial" v-else>{{ index + 1 }}</text>
							<view class="songName">
								<text class="item-songName">{{ item.name }}</text>
								<text class="horizontal">-</text>
								<text class="item-singer">{{ songSinger(item) }}</text>
							</view>
							<view class="item-menu" @click.stop="openMenu(item)">
								<text class="iconfont icon-gengduo"></text>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<!-- 弹出菜单 -->
		<u-popup class="pop-menu" v-model="menuShow" mode="bottom" border-radius="24">
			<view class="pop-menu-title">歌曲: {{ menuSong.name }}</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doNextPlay">
				<text class="iconfont icon-xiayishoubofang"></text>
				<text style="margin-left: 20rpx">下一首播放</text>
			</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doOpenSonglist">
				<text class="iconfont icon-shoucang"></text>
				<text style="margin-left: 20rpx">收藏到歌单</text>
			</view>
		</u-popup>
		<!-- 歌单菜单 -->
		<u-popup class="pop-menu" v-model="openSonglist" mode="bottom" border-radius="24">
			<view class="pop-menu-title">收藏到歌单</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doAddSonglist(favouriteList.id)">
				<u-image class="item-image" :src="favouriteList.picUrl" mode="widthFix" width="80rpx" height="80rpx" border-radius="7px"></u-image>
				<text style="margin-left: 20rpx">{{favouriteList.listName}}</text>
			</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" v-for="(item,i) in createList" :key="i" @click="doAddSonglist(item.id)">
				<u-image class="item-image" :src="item.picUrl" mode="widthFix" width="80rpx" height="80rpx" border-radius="7px"></u-image>
				<text style="margin-left: 20rpx">{{item.listName}}</text>
			</view>
		</u-popup>
		<view class="bg" :style="isBg"></view>
		<u-toast ref="uToast" />
		<play-music></play-music>
	</view>
</template>

<script>
import playMusic from '@/my-components/playMusic.vue';
import { mapMutations, mapState, mapGetters} from 'vuex'
import { songListDetail } from "@/api/platform.js";
import { existCollectSonglist, createSonglist, deleteSonglist } from "@/api/songlist.js";
import { handleSingerName } from '@/utils/songUtil.js'

export default {
    components: {
        playMusic
    },
	data() {
		return {
			// 歌单ID
			id: null,
            // 歌单平台
            musicPlatform: null,
			songlistDetail: {},
            songs: null,
			start:0,
			end:50,
			PlaysList:[],
			songList:[],
			songBg:null,
			//显示菜单
			menuShow: false,
			//显示歌单列表
			openSonglist: false,
			menuSong: {},
			//是否已收藏歌单
			isCollect: false,
			//用户收藏的歌单id
			userSonglistId: ''
		};
	},
	onLoad(option) {
		uni.showLoading({
			title: '努力加载中~',
			mask: false
		});
		this.id = option.id;
        this.musicPlatform = option.musicPlatform;
		this.getSonglistDetail();
		//判断我的歌单中是否收藏了该歌单
		existCollectSonglist({collectSonglistId: this.id}).then(data => {
			if(data != null){
				this.userSonglistId = data.id;
				this.isCollect = true;
			}
		})
	},
	components: {
		playMusic
	},
	methods: {
		...mapMutations(['setPlayList','addAndPlay','addToNext','addToSonglist','updateUserSonglist']),
		// 获得歌单数据
		async getSonglistDetail() {
			await songListDetail({playListId: this.id, musicPlatform: this.musicPlatform}).then(data=>{
                this.songlistDetail = data;
                this.songs = this.songlistDetail.songs;
                uni.hideLoading();
            })
		},
		// 播放该列表
		playSongList(index) {
            let cloneSongList = this.$u.deepClone(this.songs);
			this.setPlayList({list: cloneSongList, i: index});
		},
		// 得到bg index
		newTouchstart(index){
			this.songBg = index;
		},
        onreachBottom() {
            console.log('没有更多了')
        },
		// 处理播放数
		isPlayCount(count) {
			return count > 100000 ? (count / 10000).toFixed() + "万" : count;
		},
		//打开菜单
		openMenu(item) {
			this.menuSong = item;
			this.menuShow = true;
		},
		//打开歌单菜单
		doOpenSonglist() {
			this.menuShow = false;
			this.openSonglist = true;
		},
		//收藏歌曲到歌单
		doAddSonglist(id) {
			this.addToSonglist({songlistId: id, songId: this.menuSong.id});
			this.openSonglist = false;
		},
		//下一首播放
		doNextPlay() {
			this.addToNext(this.menuSong);
			this.menuShow = false;
		},
		//收藏歌单
		async doCollectSonglist() {
			if(this.isCollect){
				await deleteSonglist(this.userSonglistId).then(data =>{
					this.isCollect = false;
					this.$refs.uToast.show({
						title: '取消收藏',
						duration: 1000,
						position: 'bottom'
					});
				})
				this.updateUserSonglist();
			} else {
				let list = {};
				console.log(this.id)
				list['collectListId'] = this.id;
				list['listName'] = this.songlistDetail.playList.name;
				list['listDescription'] = this.songlistDetail.playList.summary;
				list['picUrl'] = this.songlistDetail.playList.picUrl;
				list['songCount'] = this.songlistDetail.playList.trackCount;
				list['type'] = '3';
				await createSonglist(list).then(data => {
					this.userSonglistId = data.id;
					this.isCollect = true;
					this.$refs.uToast.show({
						title: '已收藏',
						duration: 1000,
						position: 'bottom'
					});
				})
				this.updateUserSonglist();
			}
		}
	},
	computed:{
		...mapState(['isPlay', 'playingIndex','createList','favouriteList']),
        ...mapGetters(['getCurrentSong']),
        // 处理歌手名字
		songSinger() {
			return (song) => {
                return handleSingerName(song);
            }
		},
        // 播放列表中的歌曲是不是当前显示歌单中的
        inPlayList(item) {
			return (item) => {
				if(this.songs == null) {
                return false;
				}
				if(this.getCurrentSong == null){
					return false;
				}
				return item.id == this.getCurrentSong.id;
			}
        },
		//歌单背景
		isBg() {
			if(this.songlistDetail.playList == null) {
				return;
			}
			return `background: url(${this.songlistDetail.playList.picUrl}) left;`;
		}
	}
};
</script>

<style lang="scss" scoped>
.navbar {
	/deep/ .u-border-bottom:after {
		border-bottom-width: 0px;
	}
}
.playListInfo {
	display: flex;
	z-index: -1;
	.imgBox {
		position: relative;
        margin: 100rpx 20rpx 100rpx 40rpx;
        .playNumber {
          padding: 4rpx 10rpx;
          position: absolute;
          top: 0;
          right: 0;
          font-size: 24rpx;
          color: #fff;
        }
		.img {
			display: inline-block;
			border-radius: 24rpx;
			width: 200rpx;
			height: 200rpx;
		}
    }
	.playListSummary {
		// position: absolute;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		width: 60%;
		height: 200rpx;
		margin-top: 100rpx;
		.listName {
			font-size: 32rpx;
			font-weight: 400;
			color: #fff;
			overflow: hidden;
		}
		.listDesc {
			font-size: 24rpx;
			// font-weight: 400;
			color: rgb(219, 219, 219);
			overflow: hidden;
			text-overflow: ellipsis; 
			white-space: nowrap;
		}
	}
}
.scroll {
	display: flex;
	flex-direction: column;
	// height: calc(100vh - var(--window-top));
	width: 100%;
}
.popup {
	position: absolute;
	display: flex;
	top: calc(36vh - var(--window-top));
	 /* #ifdef MP-WEIXIN */
	top: calc(36vh - var(--window-top));
	 /* #endif */
	width: 100%;
	height: 90%;
	background-color: #fff;
	padding: 0rpx;
	// border-radius: 30rpx 30rpx 0 0;
	box-sizing: border-box;
}
.song-list {
	.song-list-item {
		display: flex;
		margin-bottom: 20rpx;
		padding: 5rpx 10rpx;
		border-radius: 0px;
		height: 80rpx;
		.song-list-info {
			position: relative;
			flex: 1;
			display: flex;
			// flex-flow: column;
			line-height: 50rpx;
			margin-left: 22rpx;
			padding-top: 8rpx;
			.songIcon {
				position: absolute;
				right: 10rpx;
				top: 50%;
				transform: translateY(-55%);
				display: flex;
				justify-content: center;
				align-items: center;
				padding-left: 5rpx;
				// padding-bottom: 2rpx;
				width: 46rpx;
				height: 46rpx;
				border-radius: 50%;
				border: 1px solid #e3e3e3;
			}
			.songIcon1 {
				position: absolute;
				right: 10rpx;
				top: 50%;
				transform: translateY(-55%);
			}
			.song-serial {
				text-align: center;
				width: 36rpx;
				font-size: 30rpx;
				margin-right: 20rpx;
			}
			.song-serial-icon {
				text-align: center;
				width: 36rpx;
				font-size: 30rpx;
				margin-right: 20rpx;
				color: rgb(255, 61, 61);
			}
			.songName {
				width: 440rpx;
				overflow: hidden;
				white-space:nowrap;
				text-overflow: ellipsis;
				.item-songName {
					color: #222;
					font-size: 30rpx;
					font-weight: 500;
				}
				.horizontal {
					margin: 0 6rpx;
					color: #666;
				}
				.item-singer {
					color: rgb(161, 151, 151);
					font-size: 24rpx;
				}
			}
			.item-menu {
				margin-left: auto;
				margin-right: 20rpx;
			}
		}
	}
}
.bg {
	position: fixed;
	top: 0px;
	left: 0px;
	height: 40%;
	width: 110%;
	margin: -5%;
	background-size: cover;
	-webkit-filter: blur(6px) brightness(1.0);
	-moz-filter: blur(6px) brightness(1.0);
	filter: progid:DXImageTransform.Microsoft.Blur(PixelRadius=10, MakeShadow=false); /* IE6~IE9 */
	z-index: -99;
}
.playall {
	display: flex;
	height: 100rpx;
	width: 100%;
	align-items: center;
	background-color: #fff;
	.playicon {
		font-size: 42rpx;
		margin: 6rpx 26rpx;
		color: rgb(255, 61, 61);
	}
	.playtext {
		font-size: 30rpx;
		font-weight: 600;
	}
	.collect-icon {
		margin-left: auto;
		margin-right: 30rpx;
		font-size: 40rpx;
	}
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
		display: flex;
		align-items: center;
	}
}
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>

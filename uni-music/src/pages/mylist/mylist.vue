<template>
	<view class="newsong-page">
		<u-navbar
			class="navbar"
			:background="{ backgroundColor: '#D83D34' }"
			back-icon-color="#fff"
			back-icon-size="38"
			back-text="我的音乐"
            z-index="0"
			:back-text-style="{ color: '#fff', fontSize: '34rpx', marginLeft: '10rpx' }"
		></u-navbar>
		<view v-if="songs.length > 0" class="scroll">
			<!-- 歌单列表 -->
			<scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
				<!-- 歌曲信息 -->
				<view class="playListInfo">
					<u-image class="img" width="100%" height="400rpx" :src="songs[0].picUrl"></u-image>
				</view>
				<!-- 播放全部 -->
				<view class="playall" hover-class="click-bg" hover-stay-time="200" @click="playSongList(0)">
					<text class="iconfont icon-playall playicon"></text>
					<text class="playtext">播放全部 ({{songs.length}})</text>
				</view>
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
			<!-- 弹出菜单 -->
			<u-popup class="pop-menu" v-model="menuShow" mode="bottom" border-radius="24">
				<view class="pop-menu-title">歌曲: {{ menuSong.name }}</view>
				<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doNextPlay">
					<text class="iconfont icon-xiayishoubofang"></text>
					<text style="margin-left: 20rpx">下一首播放</text>
				</view>
				<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doDelToSonglist">
					<text class="iconfont icon-delete"></text>
					<text style="margin-left: 20rpx">移除歌单</text>
				</view>
			</u-popup>
		</view>
		<play-music></play-music>
	</view>
</template>

<script>
import playMusic from '@/my-components/playMusic.vue';
import { mapMutations, mapState, mapGetters} from 'vuex'
import { userSonglistDetail } from "@/api/songlist.js";
import { handleSingerName } from '@/utils/songUtil.js'

export default {
    components: {
        playMusic
    },
	data() {
		return {
			// 歌单ID
			id: null,
			// 歌单类型
			type: null,
			songlistDetail: {},
            songs: [],
			songBg:null,
			menuShow: false,
			menuSong: {}
		};
	},
	onLoad(option) {
		uni.showLoading({
			title: '努力加载中~',
			mask: false
		});
		this.id = option.id;
		this.type = option.type;
		this.getUserSonglistDetail();
	},
	components: {
		playMusic
	},
	methods: {
		...mapMutations(['setPlayList','addAndPlay','addToNext','delToSonglist']),
		// 获得歌单数据
		async getUserSonglistDetail() {
			await userSonglistDetail({id: this.id}).then(data=>{
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
		//移除歌单
		doDelToSonglist() {
			this.delToSonglist({songlistId: this.id, songId: this.menuSong.id});
			this.menuShow = false;
		},
		//下一首播放
		doNextPlay() {
			this.addToNext(this.menuSong);
			this.menuShow = false;
		}
	},
	computed:{
		...mapState(['isPlay', 'playingIndex']),
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
			if(this.songlistDetail == null) {
				return;
			}
			return `background: url(${this.songlistDetail.playList.picUrl}) left;`;
		}
	}
};
</script>

<style lang="scss" scoped>
.newsong-page {
    display: flex;
    flex-direction: column;
	height: calc(100vh - var(--window-top));
	width: 100%;
    .navbar {
        /deep/ .u-border-bottom:after {
            border-bottom-width: 0px;
        }
    }
    .tabSwiper {
        z-index: 0;
		position: relative;
		margin-bottom: 10rpx;
	}
    .swiper-box {
        flex: 1;
    }
}
.scroll {
	display: flex;
	flex-direction: column;
	height: calc(100vh - var(--window-top));
	width: 100%;
}
.popup {
	position: absolute;
	display: flex;
	 /* #endif */
	width: 100%;
	height: 100%;
	background-color: #fff;
	padding: 0rpx;
	// border-radius: 30rpx 30rpx 0 0;
	box-sizing: border-box;
	.playListInfo {
		display: flex;
		flex-direction: column;
		width: 100%;
		.img {
			width: 100%;
		}
	}
}
.song-list {
	display: flex;
	flex-direction: column;
	.song-list-item {
		display: flex;
		margin-bottom: 20rpx;
		padding: 5rpx 10rpx;
		border-radius: 0px;
		height: 80rpx;
		margin-right: 20rpx;
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
			}
		}
	}
}
.playall {
	position: relative;
	display: flex;
	height: 100rpx;
	width: 100%;
	align-items: center;
	background-color: #fff;
	margin-top: -40rpx;
	border-radius: 30rpx 30rpx 0 0;
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
	background-color:rgb(230, 230, 230);
}
</style>

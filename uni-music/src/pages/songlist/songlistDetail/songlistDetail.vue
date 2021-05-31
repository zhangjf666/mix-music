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
			<view class="playListInfo">
				<image :src="songlistDetail.playList.picUrl" class="img" mode="widthFix"></image>
				<view class="playListSummary">
					<view class="listName">{{ songlistDetail.playList.name }}</view>
					<view class="listDesc">{{ songlistDetail.playList.summary }}</view>
				</view>
				<view class="bg" :style="isBg"></view>
			</view>
			<!-- 播放全部吸顶 -->
			<u-sticky offset-top="0">
				<view class="playall" hover-class="click-bg" hover-stay-time="200">
					<text class="iconfont icon-playall playicon"></text>
					<text class="playtext">播放全部</text>
				</view>
			</u-sticky>
			<!-- 歌单列表 -->
			<scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
				
				<view class="song-list">
					<view class="song-list-item" v-for="(item, index) in songs" :key="item.id" @click="playSongList(index)"
					@touchstart="newTouchstart(index)" @touchend="songBg = null" :style="index === songBg ? 'background-color:rgba(0,0,0,.1)' : ''"
					>
						<view class="song-list-info">
							<view class="songName">
								<text class="item-songName" :class="playingIndex === index && inPlayList ? 'color' : ''">{{ item.name }}</text>
                                <text class="horizontal" :class="playingIndex === index && inPlayList ? 'color' : ''">-</text>
                                <text class="item-singer" :class="playingIndex === index && inPlayList ? 'color' : ''">{{ songSinger(item) }}</text>
							</view>
							<u-icon class="songIcon1" name="volume" color="#d83d34" size="44" v-if="playingIndex===index && inPlayList"></u-icon>
							<view class="songIcon" v-else>
								<u-icon name="play-right-fill" color="#d83d34" size="24"></u-icon>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<play-music></play-music>
	</view>
</template>

<script>
import playMusic from '@/my-components/playMusic.vue';
import { mapMutations, mapState, mapGetters} from 'vuex'
import { songListDetail } from "@/api/platform.js";
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
			songlistDetail: null,
            songs: null,
			isPopup: true,
			trackIds:[],
			musicId:'',
			start:0,
			end:50,
			PlaysList:[],
			songList:[],
			songBg:null
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
	},
	components: {
		playMusic
	},
	methods: {
		...mapMutations(['setPlayList']),
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
        inPlayList() {
            if(this.songs == null) {
                return false;
            }
            if(this.getCurrentSong == null){
                return false;
            }
            let inplay = false;
            this.songs.forEach(item=>{
                if(item.id == this.getCurrentSong.id){
                    inplay = true;
                    return;
                }
            })
            return inplay;
        },
		//歌单背景
		isBg() {
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
	.img {
		margin: 100rpx 20rpx 100rpx 40rpx;
		width: 200rpx;
		height: 200rpx;
		border-radius: 24rpx;
	}
	.playListSummary {
		// position: absolute;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		width: 50%;
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
	// position: absolute;
	top: calc(40vh - var(--window-top));
	 /* #ifdef MP-WEIXIN */
	top: calc(40vh - var(--window-top));
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
	z-index: -1;
}
.playall {
	display: flex;
	height: 120rpx;
	width: 100%;
	align-items: center;
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
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>

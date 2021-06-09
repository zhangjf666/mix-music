<template>
	<view class="newsong-page">
		<u-navbar
			class="navbar"
			:background="{ backgroundColor: '#D83D34' }"
			back-icon-color="#fff"
			back-icon-size="38"
			back-text="最新音乐"
            z-index="0"
			:back-text-style="{ color: '#fff', fontSize: '34rpx', marginLeft: '10rpx' }"
		></u-navbar>
        <!-- 整屏滑动头部 -->
		<view class="tabSwiper">
			<u-tabs-swiper
				ref="tabSwiper"
				:list="tabsSwiper"
				gutter="65"
				:current="current"
				active-color="#D83D34"
				font-size="28"
				swiperWidth="750"
				bar-width="60"
				@change="tabsChange"
			></u-tabs-swiper>
		</view>
		<!-- 整屏滑动页面 -->
		<swiper :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish" class="swiper-box">
			<!-- 歌曲列表 -->
			<swiper-item v-for="(item, i) in tabsSwiper" :key="i">
				<view v-if="playLists[item.name].length > 0" class="scroll">
                    <!-- 歌曲信息 -->
                    <view class="playListInfo">
                        <image :src="playLists[item.name][0].picUrl" mode="widthFix"></image>
                    </view>
                    <!-- 播放全部吸顶 -->
                    <u-sticky offset-top="0">
                        <view class="playall" hover-class="click-bg" hover-stay-time="200" @click="playSongList(0)">
                            <text class="iconfont icon-playall playicon"></text>
                            <text class="playtext">播放全部</text>
                        </view>
                    </u-sticky>
                    <!-- 歌单列表 -->
                    <scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
                        <view class="song-list">
                            <view class="song-list-item" v-for="(item, index) in playLists[item.name]" :key="item.id" @click="addAndPlay(item)"
                            @touchstart="newTouchstart(index)" @touchend="songBg = null" :style="index === songBg ? 'background-color:rgba(0,0,0,.1)' : ''"
                            >
                                <view class="song-list-info">
                                    <view class="songName">
                                        <text class="item-songName" :class="inPlayList(item) ? 'color' : ''">{{ item.name }}</text>
                                        <text class="horizontal" :class="inPlayList(item) ? 'color' : ''">-</text>
                                        <text class="item-singer" :class="inPlayList(item) ? 'color' : ''">{{ songSinger(item) }}</text>
                                    </view>
                                    <u-icon class="songIcon1" name="volume" color="#d83d34" size="44" v-if="inPlayList(item)"></u-icon>
                                    <view class="songIcon" v-else>
                                        <u-icon name="play-right-fill" color="#d83d34" size="24"></u-icon>
                                    </view>
                                </view>
                            </view>
                        </view>
                    </scroll-view>
                </view>
                <view class="bg" :style="isBg"></view>
			</swiper-item>
		</swiper>
		<play-music></play-music>
	</view>
</template>

<script>
import { recommendNewSong, topSong } from '@/api/platform.js';
import playMusic from '@/my-components/playMusic.vue';
import { mapMutations, mapState, mapGetters} from 'vuex';
import { handleSingerName } from '@/utils/songUtil.js';

export default {
    components: {
        playMusic
    },
	data() {
		return {
			tabsSwiper:[{name: '推荐'}, {name:'华语'}, {name:'欧美'}, {name:'韩国'}, {name:'日本'}],
            current: 0,
            swiperCurrent: 0,
            showDivider: false,
            // 所有分类tag中的歌单数据
            playLists: { '推荐': [], '华语': [], '欧美': [], '韩国': [], '日本': []},
            // 新歌的分类,全部:0 华语:7 欧美:96 日本:8 韩国:16
            catagory: {推荐:'0', 华语:'7', 欧美:'96', 韩国:'16', 日本:'8'},
            songBg: null
		};
	},
	onLoad(option) {
		if (this.current === 0) {
			recommendNewSong({limit:18, areaId: "0" }).then(data => {
                this.playLists[this.tabsSwiper[this.current].name].push(...data);
            });
		}
	},
	components: {
		playMusic
	},
	methods: {
		...mapMutations(['setPlayList','addAndPlay']),
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
        //整屏滑动 swiper-item左右移动，通知tabs的滑块跟随移动
		transition(e) {
			let dx = e.detail.dx;
			this.$refs.tabSwiper.setDx(dx);
		},
		//整屏滑动 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
		// swiper滑动结束，分别设置tabs和swiper的状态
		animationfinish(e) {
			let current = e.detail.current;
			this.$refs.tabSwiper.setFinishCurrent(current);
			this.swiperCurrent = current;
			this.current = current;
		},
		//整屏滑动 tabs通知swiper切换
		tabsChange(index) {
			this.swiperCurrent = index;
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
            return (item) => {
				if(this.getCurrentSong == null){
					return false;
				}
				return item.id == this.getCurrentSong.id && this.isPlay;
			}
        },
		//歌单背景
		isBg() {
			if(this.songlistDetail == null) {
				return;
			}
			return `background: url(${this.songlistDetail.playList.picUrl}) left;`;
		}
	},
    watch: {
        current() {
            if(this.playLists[this.tabsSwiper[this.current].name].length > 0) {
                return;
            }
            if (this.tabsSwiper[this.current].name === '推荐') {
				recommendNewSong({limit:18, areaId: "0" }).then(data => {
                    this.playLists[this.tabsSwiper[this.current].name].push(...data);
                });
			} else {
                topSong({areaId: this.catagory[this.tabsSwiper[this.current].name]}).then(data =>{
                    this.playLists[this.tabsSwiper[this.current].name].push(...data);
                    console.log(this.playLists)
                })
            }
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
		margin-bottom: 20rpx;
	}
    .swiper-box {
        flex: 1;
    }
}
.playListInfo {
	display: flex;
	flex-direction: column;
	height: calc(100vh - var(--window-top));
	width: 100%;
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
}
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>

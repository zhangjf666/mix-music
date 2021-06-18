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
				gutter="95"
				:current="current"
				active-color="#D83D34"
				font-size="28"
				bar-width="50"
				@change="tabsChange"
			></u-tabs-swiper>
		</view>
		<!-- 整屏滑动页面 -->
		<swiper :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish" class="swiper-box">
			<!-- 歌曲列表 -->
			<swiper-item v-for="(item, i) in tabsSwiper" :key="i">
				<view v-if="playLists[item.name].length > 0" class="scroll">
                    <!-- 歌单列表 -->
                    <scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
						<!-- 歌曲信息 -->
						<view class="playListInfo">
							<u-image class="img" width="100%" height="400rpx" :src="playLists[item.name][0].picUrl"></u-image>
						</view>
						<!-- 播放全部吸顶 -->
						<view class="playall" hover-class="click-bg" hover-stay-time="200" @click="playSongList(0)">
                            <text class="iconfont icon-playall playicon"></text>
                            <text class="playtext">播放全部 ({{playLists[item.name].length}})</text>
                        </view>
                        <view class="song-list">
                            <view class="song-list-item" v-for="(item, index) in playLists[item.name]" :key="item.id" @click="addAndPlay(item)"
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
			</swiper-item>
		</swiper>
		<!-- 弹出菜单 -->
		<u-popup class="pop-menu" v-model="menuShow" mode="bottom" border-radius="24">
			<view class="pop-menu-title">歌曲: {{ menuSong.name }}</view>
			<view class="pop-menu-item" hover-class="click-bg" hover-stay-time="200" @click="doNextPlay">
				<text class="iconfont icon-xiayishoubofang"></text>
				<text style="margin-left: 20rpx">下一首播放</text>
			</view>
			<view class="pop-menu-item" v-if="loginFlag" hover-class="click-bg" hover-stay-time="200" @click="doOpenSonglist">
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
            songBg: null,
			//显示菜单
			menuShow: false,
			//显示歌单列表
			openSonglist: false,
			menuSong: {}
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
		...mapMutations(['setPlayList','addAndPlay','addToNext','addToSonglist']),
		// 播放该列表
		playSongList(index) {
            let cloneSongList = this.$u.deepClone(this.playLists[this.tabsSwiper[this.current].name]);
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
		//收藏到歌单
		doAddSonglist(id) {
			this.addToSonglist({songlistId: id, songId: this.menuSong.id});
			this.openSonglist = false;
		},
		//下一首播放
		doNextPlay() {
			this.addToNext(this.menuSong);
			this.menuShow = false;
		}
	},
	computed:{
		...mapState(['isPlay', 'playingIndex','createList','favouriteList']),
        ...mapGetters(['getCurrentSong','loginFlag']),
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
		margin-bottom: 10rpx;
	}
    .swiper-box {
        flex: 1;
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
		display: flex;
		align-items: center;
	}
}
.click-bg {
	background-color:rgb(230, 230, 230);
}
</style>

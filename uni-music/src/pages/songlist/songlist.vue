<template>
    <view class="songListPage">
        <u-navbar
			:background="{ backgroundColor: '#d83d34' }"
			back-icon-color="#fff"
			back-icon-size="38"
			back-text="歌单广场"
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
			<!-- 歌单 -->
			<swiper-item v-for="(item, i) in tabsSwiper" :key="i">
				<scroll-view scroll-y style="height: 100%;width: 100%;" class="swiper-item" @scrolltolower="onreachBottom">
					<view class="imgBox" v-for="list in playLists[item.name]" :key="list.id" @click="goPlaylistDetails(list.id)">
						<u-image :src="list.picUrl" mode="widthFix" width="100%" height="100%" border-radius="7px"></u-image>
						<view class="playNumber">
							<text class="iconfont icon-bofang" style="font-size: 24rpx;"></text>
							{{ isPlayCount(list.playCount) }}
						</view>
						<view class="song-title">
							<text>{{ list.name }}</text>
						</view>
					</view>
					<u-divider v-if="showDivider">到底啦</u-divider>
				</scroll-view>
			</swiper-item>
		</swiper>
		<play-music></play-music>
    </view>
</template>

<script>
import playMusic from '@/my-components/playMusic.vue';
import { allTags, highQualityTags, hotTags, recommendSongList,categoryList, highQualityList} from "@/api/platform.js";

export default {
    components: {
		playMusic
	},
    data() {
        return {
            tabsSwiper:[{name: '推荐'}, {name:'精品'}],
            current: 0,
            swiperCurrent: 0,
            // 加载每页条数
            limit: 24,
            showDivider: false,
            // 所有分类tag中的歌单统计数据
            songListSummary: {},
            // 所有分类tag中的歌单数据
            playLists: { '推荐': [], '精品': []},
            // 歌单种类
            catagory: [{0:'语种'},{1:'风格'},{2:'场景'},{3:'情感'},{4:'主题'}],
            // 歌单分类标签
            allTag: [],
            // 热门分类标签
            hotTag: []
        }
    },
    async onLoad() {
        await this.getAllTags();
        await this.getHotTags();
        this.hotTag.forEach(tag => {
            this.$set(this.playLists, tag.name, []);
            this.tabsSwiper.push({name: tag.name});
        });
        if (this.current === 0) {
			await recommendSongList({limit:50, musicPlatform: "1" }).then(data => {
                let summary = {total:50, more:false};
                this.playLists[this.tabsSwiper[this.current].name].push(...data);
                this.songListSummary[this.tabsSwiper[this.current].name] = summary;
            });
		}
	},
	mounted() {
		
	},
    methods: {
        // 获取所有歌单分类
        async getAllTags() {
            await allTags().then(data => {
                this.allTag = data;
            })
        },
        // 获取热门歌单分类
        async getHotTags() {
            await hotTags().then(data => {
                this.hotTag = data;
            })
        },
        //加载分类歌单
        async getSongList(index) {
            let summary = this.songListSummary[this.tabsSwiper[index].name];
            var data = {}
            if(summary == null){
                data = {
                    cat: this.tabsSwiper[index].name,
                    order: 'hot',
                    limit: this.limit,
                    offset: 0 
                }
            } else {
                data = {
                    cat: summary.cat,
                    order: 'hot',
                    limit: this.limit,
                    offset: summary.offset
                }
            }
            await categoryList(data).then(data => {
                let list = data.playLists;
                delete data.playLists;
                if(summary == null) {
                    this.songListSummary[this.tabsSwiper[index].name] = data
                } else {
                    this.songListSummary[this.tabsSwiper[index].name].offset += list.length;
                    this.songListSummary[this.tabsSwiper[index].name].more = data.more;
                }
                this.playLists[this.tabsSwiper[index].name].push(...list);
            })
        },
        //加载精品歌单
        async getHighQualityList() {
            let summary = this.songListSummary[this.tabsSwiper[1].name];
            var data = {}
            if(summary == null){
                data = {
                    cat: '全部',
                    limit: this.limit,
                    lasttime: 0 
                }
            } else {
                data = {
                    cat: summary.cat,
                    limit: this.limit,
                    lasttime: summary.offset
                }
            }
            await highQualityList(data).then(data => {
                let list = data.playLists;
                delete data.playLists;
                if(summary == null) {
                    this.songListSummary[this.tabsSwiper[1].name] = data
                } else {
                    this.songListSummary[this.tabsSwiper[1].name].offset +=  list.length;
                    this.songListSummary[this.tabsSwiper[1].name].more = data.more;
                }
                this.playLists[this.tabsSwiper[1].name].push(...list);
                console.log(this.playLists)
            })
        },
        onreachBottom() {
            this.showDivider = false;
            if(this.songListSummary[this.tabsSwiper[this.current].name].more == false) {
                this.showDivider = true;
            } else {
                if(this.tabsSwiper[this.current].name === '精品'){
                    this.getHighQualityList();
                } else {
                    this.getSongList(this.current);
                }
            }
        },
        // 去歌单详情页
		goPlaylistDetails(id){
			uni.navigateTo({
				url:`songlistDetail/songlistDetail?id=${id}&musicPlatform=1`
			})
		},
		// 处理播放数
		isPlayCount(count) {
			return count > 100000 ? (count / 10000).toFixed() + '万' : count;
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
    computed: {
        isSongListNull() {
            return (i) => {
                if(this.playLists == null){
                    return false;
                }
                return this.playLists[this.tabsSwiper[i].name] !== null
            }
        }
    },
    watch: {
        current() {
            if(this.songListSummary[this.tabsSwiper[this.current].name] != null) {
                return;
            }
            if (this.tabsSwiper[this.current].name === '推荐') {
				return;
			}
            if(this.tabsSwiper[this.current].name === '精品') {
                this.getHighQualityList();
            } else {
                this.getSongList(this.current);
            }
        }
    }
};
</script>

<style lang="scss" scoped>
.songListPage {
	display: flex;
	flex-direction: column;
	height: calc(100vh - var(--window-top));
	width: 100%;
	.tabSwiper {
        z-index: 0;
		position: relative;
		margin-bottom: 20rpx;
	}
	.imgBox {
		margin-left: 31rpx;
		margin-bottom: 32rpx;
		position: relative;
		display: inline-block;
		width: 208rpx;
		.playNumber {
			padding: 4rpx 10rpx;
			position: absolute;
			top: 0;
			right: 0;
			font-size: 24rpx;
			color: #fff;
		}
	}
	.song-title {
		position: relative;
		width: 100%;
		height: 68rpx;
		font-size: 24rpx;
		overflow: hidden;
		white-space: normal;
		word-wrap: break-word;
		word-break: break-all; /* break-all(允许在单词内换行。) */
		text-overflow: ellipsis; /* 超出部分省略号 */
		display: -webkit-box; /** 对象作为伸缩盒子模型显示 **/
		-webkit-box-orient: vertical; /** 设置或检索伸缩盒对象的子元素的排列方式 **/
		-webkit-line-clamp: 2; /** 显示的行数 **/
	}
}
.swiper-box {
	flex: 1;
}
.swiper-item {
	// padding: 40rpx 0;
}
</style>
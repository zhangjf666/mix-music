<template>
    <view class="ranklistPage">
        <u-navbar
			:background="{ backgroundColor: '#d83d34' }"
			back-icon-color="#fff"
			back-icon-size="38"
			back-text="排行榜"
            z-index="0"
			:back-text-style="{ color: '#fff', fontSize: '34rpx', marginLeft: '10rpx' }"
		></u-navbar>

        <scroll-view scroll-y scroll-with-animation class="musicList" style="{ height: 100% }">
			<view class="top">
				<view class="top-item relative" v-for="item in topRankList" :key="item.id" @click="goPlaylistDetails(item)">
					<view class="leftImage">
                        <u-image :src="item.coverImgUrl" mode="widthFix" width="100%" height="100%" border-radius="7px"></u-image>
						<view class="updateFrequency">
							<text>{{ item.updateFrequency }}</text>
						</view>
					</view>
					<view class="rightMusic">
						<p class="title">{{ item.name }}</p>
						<p v-for="(val, index) in item.tracks" :key="index" class="music-item">
							<span class="index">{{ index + 1 }}.</span>
							<text class="musicName">{{ val.first }}</text>
							<text class="text">- {{ val.second }}</text>
						</p>
					</view>
				</view>
			</view>

			<view>
				<view class="list-item" v-for="item in rankList" :key="item.id" @click="goPlaylistDetails(item)">
                    <u-image :src="item.coverImgUrl" mode="widthFix" width="100%" height="100%" border-radius="7px"></u-image>
					<view class="updateFrequency">
						<text>{{ item.updateFrequency }}</text>
					</view>
                    <view class="list-item-text">{{ item.name }}</view>
				</view>
			</view>
		</scroll-view>
        <play-music></play-music>
    </view>
</template>

<script>
import { topListDetail } from "@/api/platform.js";
import playMusic from '@/my-components/playMusic.vue';

export default {
    components: {
        playMusic
    },
    data() {
		return {
			rankList: [],
			topRankList: []
		};
	},
	created() {
		this.getRankList();
	},
    methods: {
		async getRankList() {
			uni.showLoading({
				title: '加载中...'
			});
			await topListDetail().then(data => {
                this.topRankList = data.list.slice(0, 4);
                this.rankList = data.list.slice(4);
                uni.hideLoading();
            })
		},
		playCount(val) {
			return filterPlayCount(val);
		},
		// 去歌单详情页(排行榜也是歌单)
		goPlaylistDetails(item){
			uni.navigateTo({
				url:`../songlist/songlistDetail/songlistDetail?id=${item.id}&musicPlatform=1`
			})
		},
	}
}
</script>

<style lang="scss" scoped>
.ranklistPage {
    width: 100%;
	height: calc(100vh - var(--window-top));
    display: flex;
	flex-direction: column;
	.musicList {
		padding: 0 10rpx 0 30rpx;
		width: 100%;
        height: 95%;
		box-sizing: border-box;
        .top {
            margin: 10px 0;
            border-bottom: 1upx solid rgba(0,0,0,0.1);
            .top-item {
                .leftImage {
                    display: inline-block;
                    height: 210rpx;
                    width: 210rpx;
                    position: relative;
                }
                .rightMusic {
                    width: calc(100% - 260rpx);
                    margin-left: 40rpx;
                    display: inline-block;
                    .title {
                        font-weight: 600;
                        font-size: 32rpx;
                        margin: 8px 0;
                    }
                    .music-item {
                        width: 100%;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        margin-bottom: 12rpx;
                        .index {
                            margin-right: 3px;
                        }
                        .musicName {
                            font-size: 28rpx;
                            color: rgba(0, 0, 0, 0.7);
                            margin-right: 5px;
                        }
                        .text {
                            font-size: 12px;
                            color: #666;
                        }
                    }
                }
            }
        }
		.updateFrequency {
            padding: 4rpx 10rpx;
			position: absolute;
			top: 4rpx;
			right: 4rpx;
			font-size: 24rpx;
			border-radius: 26rpx;
			color: #fff;
			background: rgba(0, 0, 0, 0.15);
		}
		.list-item {
			width: calc(100% / 3 - 10px);
			margin: 0rpx 20rpx 14rpx 0;
			display: inline-block;
			overflow: hidden;
			position: relative;
			border-radius: 7px;

			.list-item-image {
				height: 210rpx;
				border-radius: 7px;
			}
			.list-item-text {
				width: 100%;
				padding-top: 2px;
				color: #666;
				font-size: 14px;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
                text-align: center;
			}
		}
	}
}
</style>
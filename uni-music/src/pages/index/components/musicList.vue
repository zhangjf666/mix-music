<template>
    <view class="music-list">
        <swiper :style="'height:100%'" next-margin="50rpx" v-if="currentList.length > 0">
            <swiper-item class="music-swiper-item" v-for="(item, index) in songList" :key="index">
                <view class="song-item" hover-class="click-bg" hover-stay-time="200" @click="addPlay(k)" v-for="(k) in item" :key="k.id">
                    <u-image class="item-image" v-if="k.picUrl" :src="k.picUrl" mode="widthFix" width="100" height="100" border-radius="7px"></u-image>
                    <view class="item-text">
                        <view style="width: 80%">
                            <text class="song-name">{{ k.name }}</text>
                            <text class="song-singer">- {{ songSinger(k) }}</text>
                        </view>
                        <text class="iconfont item-icon" :class="showPlayIcon(k) ? 'icon-pause':'icon-play'"></text>
                    </view>
                </view>
            </swiper-item>
        </swiper>
    </view>
</template>

<script>
import { handleSingerName } from '@/utils/songUtil.js';
import { mapState, mapMutations, mapGetters } from 'vuex';

export default {
    props: {
		currentList: {
			type: Array
		}
	},
    computed: {
        ...mapState(['playingIndex','isPlay']),
        ...mapGetters(['getCurrentSong']),
		songList() {
			let list = [];
			for (let i = 0; i < this.currentList.length; i += 3) {
				let endVal = i + 3;
				if (endVal > this.currentList.length) {
					endVal = this.currentList.length;
				}
				list.push(this.currentList.slice(i, endVal));
			}
			return list;
		},
        // 处理歌手名字
		songSinger() {
			return (song) => {
                return handleSingerName(song);
            }
		},
        showPlayIcon() {
            return (item) => {
                return this.getCurrentSong != null && this.getCurrentSong.id == item.id && this.isPlay;
            }
        }
	},
    methods: {
        ...mapMutations(['addAndPlay','setPlayingIndex','setPlayList','setShowPlayPage']),
        addPlay(item) {
            //如果当前播放 重复点击，就打开播放页面
            if(this.getCurrentSong != null && this.getCurrentSong.id == item.id){
                this.setShowPlayPage(true);
            } else {
                this.addAndPlay(item);
            }
        }
    }
}
</script>

<style lang="scss" scoped>
.music-list {
    display: flex;
    width: 100%;
    flex-direction: column;
    height: 100%;
    .music-swiper-item {
        // padding-left: 12rpx;
        box-sizing: border-box;
        .song-item {
            display: flex;
            width: 100%;
            .item-image {
                margin: 8rpx 16rpx 0 0;
            }

            .item-text {
                width: calc(100% - 160rpx);
                display: flex;
                align-items: center;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                border-bottom: 1upx solid rgba(0, 0, 0, 0.1);
                .song-name {
                    color: #000;
                    font-size: 30rpx;
                    margin-right: 5px;
                    text-overflow: ellipsis;
                }
                .song-singer {
                    font-size: 12rpx;
                    color: #666;
                    text-overflow: ellipsis;
                }
                .item-icon {
                    margin-left: auto;
                    font-size: 40rpx;
                    color: #d83d34;
                }
            }
        }
    }
}
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>
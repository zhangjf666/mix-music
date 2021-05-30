<template>
    <view class="searchList-container">
        <scroll-view scroll-y class="popup" @scrolltolower="onreachBottom">
            <div class="music-item" v-for="(item, index) in musicList" :key="index" @click="playMusic(item)">
                <image v-if="item.picUrl" :src="item.picUrl + '?param=60y60'" mode="" class="musicImg"></image>
                <div class="rightInfo">
                    <div class="music-info">
                        <span style="color:rgb(86,124,166)">{{ item.name }}</span>
                        <span v-if="item.alia && item.alia.length > 0" style="margin-left: 5px;">({{ item.alia[0] }})</span>
                    </div>
                    <div class="music-info" style="margin-top:6px;">
                        <template v-if="item.singers && item.singers.length > 0">
                            {{ songSinger(item)}}
                        </template>
                        <span v-if="item.albums && item.albums.length > 0" style="margin-left: 5px;">- {{ item.albums[0].name }}</span>
                    </div>
                </div>
            </div>
        </scroll-view>
    </view>
</template>

<script>
import { search } from "@/api/platform.js";
import { mapState, mapMutations } from 'vuex';
import { handleSingerName } from '@/utils/songUtil.js'

export default {
	data() {
		return {
            //搜索建议
			musicList: [],
            // 控制背景
			clickBg:'',
		};
	},
    methods: {
        ...mapMutations(['addAndPlay']),
        getSearchData(keywords) {
            search({keyword: keywords}).then(data => {
                this.musicList = data;
            })
        },
        onreachBottom() {
            console.log('1111')
        },
        //播放点击的歌曲
        playMusic(song) {
            this.addAndPlay(song);
        }
    },
    computed: {
        // 处理歌手名字
		songSinger() {
			return (song) => {
                return handleSingerName(song);
            }
		}
    }
}
</script>


<style lang="scss" scoped>
.searchList-container {
    .popup {
        width: 100%;
        height:100%;
        position: fixed;
    }
    .music-item {
        border-bottom: 1upx solid rgba(0, 0, 0, 0.1);
        padding-bottom: 6px;
        margin-top: 8px;
        color: rgb(133, 133, 133);
        align-items: center;
        display: flex;
        .musicImg {
            width: 75rpx;
            height: 75rpx;
            border-radius: 6px;
            margin-right: 25rpx;
        }
        .rightInfo {
            width: calc(100% - 90rpx);
        }
        .music-info {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 90%;
        }
    }
}
</style>
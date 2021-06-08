<template>
    <view class="searchList-container">
        <scroll-view scroll-y class="popup" :scroll-top="scrollTop" @scroll="scroll" @scrolltolower="onreachBottom">
            <view class="music-item" v-for="(item, index) in searchData.data" :key="index" @click="playMusic(item)">
                <image v-if="item.picUrl" :src="item.picUrl + '?param=60y60'" mode="" class="musicImg"></image>
                <view class="rightInfo">
                    <view class="music-info">
                        <span style="color:rgb(86,124,166)">{{ item.name }}</span>
                        <span v-if="item.alia && item.alia.length > 0" style="margin-left: 5px;">({{ item.alia[0] }})</span>
                    </view>
                    <view class="music-info" style="margin-top:6px;">
                        <template v-if="item.singers && item.singers.length > 0">
                            {{ songSinger(item)}}
                        </template>
                        <span v-if="item.albums && item.albums.length > 0" style="margin-left: 5px;">- {{ item.albums[0].albumName }}</span>
                    </view>
                </view>
            </view>
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
            // 返回顶部
            scrollTop: 0,
            oldScrollTop: 0,
            // 搜索内容
			searchData: {},
            // 控制背景
			clickBg:'',
            // 每页数量
            limit: 20
		};
	},
    methods: {
        ...mapMutations(['addAndPlay']),
        async getSearchData(keywords) {
            uni.showLoading({
                title: '努力加载中~',
                mask: false
            });
            if(this.searchData == null || (keywords != '' && this.searchData.keywords != keywords)){
                await search({keyword: keywords, }).then(data => {
                    this.searchData = data;
                    this.goTop();
                })
            } else if(this.searchData.more) {
                await search({ keyword: this.searchData.keywords,
                    limit: this.limit, offset: this.searchData.offset}).then(data => {
                        this.searchData.more = data.more;
                        this.searchData.total = data.total;
                        this.searchData.offset = data.offset;
                        this.searchData.data.push(...data.data);
                    })
            }
            uni.hideLoading();
        },
        onreachBottom() {
            this.getSearchData('');
        },
        //播放点击的歌曲
        playMusic(song) {
            this.addAndPlay(song);
        },
        scroll(e) {
            this.oldScrollTop = e.detail.scrollTop
        },
        goTop() {
            this.scrollTop = this.oldScrollTop
            this.$nextTick(function() {
                this.scrollTop = 0
            });
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
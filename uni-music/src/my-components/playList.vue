<template>
	<!-- 播放列表 -->
	<view class="playList">
		<u-popup v-model="isShow" mode="bottom" border-radius="20" height="60%" :custom-style="style" safe-area-inset-bottom>
			<view class="playListHead">
				<view class="playListTitle">
					当前播放
					<text>({{ playlistLength }})</text>
				</view>
				<view class="playLoop">
					<text class="iconfont icon-listLoop" style="margin-right: 10rpx;"></text>
					列表循环
				</view>
			</view>
			<view class="playListBody">
				<view
					class="playListBody-item"
					v-for="(item, i) in playlist"
					:key="item.id"
                    @click="playIndexSong(i)"
					@touchstart="listTouchstart(i)"
					@touchend="songBg = null"
					:style="i === songBg ? 'background-color:rgba(0,0,0,.2)' : ''"
				>
					<view class="item-info">
						<text class="songIcon iconfont icon-laba" v-if="i === playingIndex"></text>
						<view class="item-text" :class="playingIndex === i ? 'color' : ''">
							<text class="item-songName" :class="playingIndex === i ? 'color' : ''">{{ item.name }}</text>
							<text class="horizontal" :class="playingIndex === i ? 'color' : ''">-</text>
							<text class="item-singer" :class="playingIndex === i ? 'color' : ''">{{ item.singerName }}</text>
						</view>
					</view>
					<view><text class="iconfont icon-close" @click.stop="removePlayListSong(i)"></text></view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex';

export default {
	name: 'playList',
	data() {
		return {
			// popup样式
			style: {
				width: '90%',
				margin: '0 auto 36rpx',
				borderRadius: '20rpx'
			},
			//歌曲列表背景控制
			songBg: null
		};
	},
	methods: {
		...mapMutations(['setPlayingIndex','setShowPlayList', 'setPlayMode', 'removePlayListSong']),
		// 获得要播放音乐的索引
		playIndexSong(index) {
			this.setPlayingIndex(index);
		},
		listTouchstart(index){
			this.songBg = index;
		}
	},
	computed: {
        ...mapGetters(['playlistLength']),
		...mapState(['playlist', 'isShowPlaylist', 'playingIndex']),
        isShow: {
            get() {
                return this.isShowPlaylist;
            },
            set(val) {
                this.setShowPlayList(val);
            }
        }
	},
	watch:{
	}
};
</script>

<style lang="scss" scoped>
.playList {
		.playListHead {
			position: fixed;
			top: 0;
			width: 100%;
			padding: 25rpx 20rpx 40rpx 20rpx;
			background-color: #fff;
			display: flex;
			justify-content: space-between;
			align-items: flex-end;
			.playListTitle {
				font-size: 36rpx;
				color: #222;
				font-weight: 600;
				text {
					margin-left: 10rpx;
					font-size: 28rpx;
					color: #666;
				}
			}
		}
		.playListBody {
			margin-top: 112rpx;
			.playListBody-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				height: 85rpx;
				padding: 0 20rpx;
				.item-info {
					display: flex;
					max-width: 70%;
					// color: #d83d34 !important;
                    .songIcon {
                        margin-right: 10rpx;
                        color: #d83d34;
                        font-size: 42rpx;
                        margin-top: -4rpx;
                    }
					.item-text{
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
					}
					.color {
						color: #d83d34 !important;
					}
				}
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
					color: #666;
					font-size: 24rpx;
				}
				.icon-close {
					color: #666;
					margin-right: 10rpx;
				}
			}
		}
	}
</style>

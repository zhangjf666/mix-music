<template>
	<view class="hotSearch-container">
		<text style="font-weight: 600;font-size: 28rpx;margin-bottom: 10px;">热搜榜</text>
		<view class="hotList">
			<view class="hot-item flex" 
            :class="hotSearchBg === index ? 'hotSearchBg' : ''" 
            v-for="(item, index) in hotList" 
            :key="index" 
            @click="handleClick(item.searchWord)"
            @touchstart="touchstart(index)"
			@touchend="touchend">
				<span class="index" :class="{ topThree: index <= 2 }">{{ index + 1 }}</span>
				<span class="searchLabel">{{ item.searchWord }}</span>
				<image v-if="item.iconUrl" :src="item.iconUrl" mode="" class="iconImg" :class="item.iconType == 5 ? 'topImg' : 'hotImg'"></image>
			</view>
		</view>
	</view>
</template>

<script>
import { searchHotDetail } from "@/api/platform.js";

export default {
	data() {
		return {
			hotList: [],
            // 控制背景
			hotSearchBg:'',
		};
	},
	created() {
		this.getDetailSearch();
	},
	methods: {
		//获取热门搜索详细列表
		getDetailSearch() {
			searchHotDetail().then(data => {
                this.hotList = data.data
            })
		},
		handleClick(keyword) {
			this.$emit('chooseKey', keyword);
		},
        // 触摸事件
		touchstart(index){
			this.hotSearchBg=index;
		},
		touchend(){
			this.hotSearchBg='';
		}
	}
};
</script>

<style lang="scss" scoped>
$bColor: #d83d34;
.hotSearch-container {
	width: 100%;
	position: relative;
	margin-top: 20rpx;
	.hotList {
		margin-top: 15rpx;
		width: 100%;
		flex-wrap: wrap;
		.hot-item {
			width: 50%;
            height: 80rpx;
			padding-top: 14rpx;
			display: inline-block;
			&.topThree {
				.index {
					color: $bColor;
				}
				.word {
					font-family: 'Microsoft Yahei', Arial, Helvetica, sans-serif;
					font-weight: 600;
					color: #303030;
				}
			}
			.index {
				width: 44rpx;
				display: inline-block;
                vertical-align: middle;
			}
			.searchLabel {
				max-width: calc(100% - 125rpx);
				display: inline-block;
				white-space: nowrap;
				vertical-align: middle;
				overflow: hidden;
				text-overflow:ellipsis;
			}

			.iconImg {
				margin-left: 15rpx;
				vertical-align: middle;

				&.topImg {
					width: 30rpx;
					height: 32rpx;
					vertical-align: text-bottom;
				}
				&.hotImg {
					width: 56rpx;
					height: 28rpx;
				}
			}
		}
	}
    .hotSearchBg{
		background-color:rgba(0,0,0,.1);
	}
}
</style>

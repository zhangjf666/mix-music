<template>
    <view class="search-suggest">
        <view class="suggestList" @click.self="handleClose">
			<view class="suggestMain">
				<view class="suggest-item" style="color:rgb(86,124,166)" @click="searchClick(keywords)">
					搜索 " {{keywords}} "
				</view>
				<view class="suggest-item"
                    :class="clickBg === index ? 'clickBg' : ''" 
                    @touchstart="touchstart(index)"
			        @touchend="touchend" 
                    v-for="(item,index) in suggestList" 
                    :key="index" 
                    @click="searchClick(item.keyword)">
					<text class="iconfont icon-search" style="margin-right: 10rpx;"></text>
					    {{item.keyword}}
				</view>
			</view>
		</view>
    </view>
</template>

<script>
import { searchSuggest } from "@/api/platform.js";

export default {
    props:{
        search:{
            type:String,
            default:''
        }
    },
	data() {
		return {
            //搜索值
            keywords: '',
            //搜索建议
			suggestList: [],
            // 控制背景
			clickBg:'',
		};
	},
	methods: {
		//获取热门搜索详细列表
		getSearchSuggest() {
            if(!this.keywords.trim()){
                return;
            }
			searchSuggest({keywords: this.keywords}).then(data => {
                this.suggestList = data.result.allMatch;
            })
		},
		searchClick(keyword) {
			this.$emit('chooseKey', keyword);
		},
        // 触摸事件
		touchstart(index){
			this.clickBg=index;
		},
		touchend(){
			this.clickBg='';
		}
	},
    watch:{
        search(keywords){
            this.keywords = keywords;
            this.getSearchSuggest()
        }
    }
};
</script>

<style lang="scss" scoped>
.search-suggest {
    width: 100%;
	position: relative;
    .suggestList{
        background-color: rgba(255,255,255,0.1);
		width: 100%;
		flex-wrap: wrap;
        .suggestMain{
            z-index: 0;
            background-color: #fff;
            .suggest-item{
                padding: 24rpx 0;
                border-bottom: 1upx solid rgba(0, 0, 0, 0.1);
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                box-sizing: border-box;
                width: 100%;
            }
        }
    }
    .clickBg{
		background-color:rgba(0,0,0,.1);
	}
}
</style>
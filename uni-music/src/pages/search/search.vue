<template>
    <view class='search'>
        <!-- 头部导航栏 -->
        <u-navbar z-index="0" back-icon-color="#fff" back-icon-size="40" :background="{background: '#d83d34'}">
			<view class="slot-wrap">
				<u-search @search="getSearch" @focus="getFocue" :show-action="false" color="rgba(255,255,255,.9)" placeholder-color="rgba(255,255,255,.5)" bg-color="" search-icon="" :placeholder="defaultSearch" v-model="search"></u-search>
			</view>
		</u-navbar>
        <!-- 内容显示 -->
        <view class="content-list">
            <search-hot v-show="searchHotVisible" @chooseKey="chooseKey"></search-hot>
            <search-suggest v-show="searchSuggestVisible" :search="search" @chooseKey="chooseKey"></search-suggest>
            <search-list ref="searchComponent" v-show="searchListVisible"></search-list>
        </view>
        <play-music></play-music>
    </view>
</template>

<script>
import playMusic from '@/my-components/playMusic.vue';
import { mapMutations, mapState } from 'vuex';
import { searchDefaultKeyword } from "@/api/platform.js";
import searchHot from "./components/searchHot.vue";
import searchSuggest from "./components/searchSuggest.vue";
import searchList from "./components/searchList.vue";

export default {
    data() {
		return {
            //搜索词
			search:'',
			//默认搜索词
            defaultSearch:'',
            //显示热搜列表
            searchHotVisible: true,
            //显示搜索建议
            searchSuggestVisible: false,
            //显示搜索列表页
            searchListVisible: false,
            //是否是点击热搜列表或者所搜建议进行搜索
            isSupport: false
		};
	},
    components: {
		playMusic, searchHot, searchSuggest, searchList
	},
    onLoad(){
		this.getSearchDefault();
	},
    methods: {
        // 获得默认搜索词
		async getSearchDefault(){
            await searchDefaultKeyword().then(data => {
                this.defaultSearch = data.data.realkeyword;
            })
		},
        // 搜索触发事件
		getSearch(keywords){
			this.searchHotVisible = false;
            this.searchSuggestVisible = false;
            this.searchListVisible = true;
            this.$refs.searchComponent.getSearchData(keywords);
		},
        // 搜索框获得输入焦点时
        getFocue(keyword) {
            this.updateComponent()
        },
        //点击热搜列表
        chooseKey(keywords){
            //不更新子组件状态
            this.isSupport = true;
            this.search = keywords;
            this.getSearch(keywords);
        },
        //更新子组件状态
        updateComponent() {
            if(this.isSupport) {
                this.isSupport = !this.isSupport;
                return;
            }
            if(this.search == '' || this.search.lenght == 0){
                this.searchHotVisible = true;
                this.searchSuggestVisible = false;
                this.searchListVisible = false;
            } else {
                this.searchHotVisible = false;
                this.searchSuggestVisible = true;
                this.searchListVisible = false;
            }
        }
    },
    watch: {
        search() {
            this.updateComponent();
        }
    }
}
</script>

<style lang="scss" scoped>
    $bColor: #d83d34;
	.slot-wrap{
		width: 95%;
		/deep/.u-content{
			border-radius: 0 !important;
			padding: 0;
			border-bottom: 1px solid rgba($color: #fff, $alpha: .5);
		}
	}
    .content-list{
        padding: 0 15px;
		box-sizing: border-box;
		width: 100%;
        height: 90%;
		margin-top:5px;
	}
</style>
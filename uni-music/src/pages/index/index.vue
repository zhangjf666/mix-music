<template>
	<view class="content">
		<u-search placeholder="" v-model="keyword" :clearabled="true" @search="doSearch"></u-search>
		<!-- <u-tabs :list="list" :is-scroll="false" bar-width="140" :current="currentTab" @change="change"> -->
		<view class="zj" v-for="(item) in recommendList" v-bind:key="item.id">
			<u-image width="20%" height="140rpx" :src="item.picUrl"></u-image>
			<p>{{item.name}}</p>
			<p>{{item.playCount}}</p>
			<p>{{item.trackCount}}</p>
		</view>
		<!-- </u-tabs> -->
	</view>
</template>

<script>
	import { recommend, search } from '@/api/platform.js'
	export default {
		data() {
			return {
				keyword: '遥看瀑布挂前川',
				title: 'Hello',
				list: [{
					name: '推荐'
				}, {
					name: '所有歌单'
				}],
				currentTab: 0,
				src: 'https://cdn.uviewui.com/uview/example/fade.jpg',
				recommendList: [],
				searchList: []
			}
		},
		onLoad() {
			recommend({musicPlatform :'1'}).then(data => {
				this.recommendList = data
			})
		},
		methods: {
			change(index){
				this.currentTab = index;
				recommend({musicPlatform :'1'}).then(data => {
					this.recommendList = data
					console.log(this.recommendList)
				})
			},
			doSearch(keyword){
				search({keyword: keyword}).then(data => {
					this.searchList = data
					console.log(this.searchList)
				})
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		/* align-items: center;
		justify-content: center; */
	}

	.zj {
			margin-bottom: 30upx;
			.imgSpan {
				position:relative;
				.img{
					display: block;
					width: 100%;
				}
				.icon{
					position: absolute;
					z-index: 1;
					right: 10upx;
					bottom: 10upx;
					width: 40upx;
					opacity: .9;
				}
				.listenCount{
					position: absolute;
					left: 10upx;
					bottom: 10upx;
					color: rgba(255,255,255,.8);
					font-size: 20upx;
					
					image{
						width: 28upx;
						height: 32upx;
						margin-right: 6upx;
						display: inline-block;
						vertical-align: text-bottom;
					}
				}
			}
		}

	.text-area {
		display: flex;
		justify-content: center;
	}
</style>

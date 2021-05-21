<template>
  <view class="home">
    <!-- 头部导航栏 -->
    <!-- #ifndef MP-WEIXIN -->
    <u-navbar
      back-icon-name=""
      :background="{ background: '#d83d34' }"
      height="0"
      class="navbar"
    ></u-navbar>
    <!-- #endif -->
    <!-- 整屏滑动头部 -->
    <view class="tabSwiper">
      <!-- #ifndef MP-WEIXIN -->
      <u-tabs-swiper
        ref="tabSwiper"
        :list="tabsSwiper"
        gutter="60"
        active-color="#fff"
        :show-bar="false"
        :current="current"
        font-size="36"
        swiperWidth="750"
        inactive-color="rgba(255,255,255,.8)"
        bg-color="#D83D34"
        @change="tabsChange"
      ></u-tabs-swiper>
      <!-- #endif -->
      <!-- #ifdef MP-WEIXIN -->
      <u-tabs-swiper
        ref="tabSwiper"
        :list="tabsSwiper"
        gutter="60"
        active-color="#fff"
        :show-bar="false"
        :current="current"
        font-size="36"
        swiperWidth="750"
        inactive-color="#fff"
        bg-color="#D83D34"
        @change="tabsChange"
      ></u-tabs-swiper>
      <!-- #endif -->
      <text class="iconfont icon-liebiao" @click="isLogIn = true"></text>
      <text class="iconfont icon-search" @click="goSearch"></text>
    </view>
	<!-- 整屏滑动页面 -->
      <swiper
        :current="swiperCurrent"
        @transition="transition"
        @animationfinish="animationfinish"
		duration="300"
        class="swiper-box"
      >
        <!-- 我的 -->
        <swiper-item> 我的 </swiper-item>
        <!-- 发现页面 -->
        <swiper-item>
			<scroll-view scroll-y class="swiper-item">
				
			</scroll-view>
		</swiper-item>
        <!-- 电台 -->
        <swiper-item> 电台 </swiper-item>
      </swiper>
  </view>
</template>

<script>
import { recommend, search } from "@/api/platform.js";
export default {
  data() {
    return {
      isLogIn: false,
      tabsSwiper: [{ name: "我的" }, { name: "发现" }, { name: "电台" }],
      current: 1,
      swiperCurrent: 1,
    };
  },
  onLoad() {
    // recommend({musicPlatform :'1'}).then(data => {
    // 	this.recommendList = data
    // })
  },
  methods: {
    getRecommendList() {
      recommend({ musicPlatform: "1" }).then((data) => {
        this.recommendList = data;
        console.log(this.recommendList);
      });
    },
    //整屏滑动 tabs通知swiper切换
    tabsChange(index) {
      this.swiperCurrent = index;
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
    // 跳转搜索页面
    goSearch() {
      uni.navigateTo({
        url: `../search/search`,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
$bColor: #d83d34;
.navbar {
  /deep/ .u-border-bottom:after {
    border-bottom-width: 0px;
  }
}
.tabSwiper {
  margin-top: -1px;

  /deep/ .u-tabs-scroll-box {
    text-align: center;
    height: 90rpx;
    line-height: 90rpx;
  }
  .icon-liebiao {
    position: absolute;
    top: 17rpx;
    /* #ifdef APP-PLUS */
    top: 4.5%;
    /* #endif */
    left: 35rpx;
    color: #fff;
    font-size: 44rpx;
    z-index: 99;
  }
  .icon-search {
    position: absolute;
    top: 17rpx;
    /* #ifdef APP-PLUS */
    top: 4.5%;
    /* #endif */
    right: 35rpx;
    color: #fff;
    font-size: 44rpx;
    z-index: 99;
  }
}
.home {
  display: flex;
  flex-direction: column;
  height: calc(100vh - var(--window-top));
  width: 100%;
}
.swiper-box {
  flex: 1;
}
.swiper-item {
	height: 100%;
}
</style>

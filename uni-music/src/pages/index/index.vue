<template>
  <view class="home">
    <!-- 头部导航栏 -->
    <!-- #ifndef MP-WEIXIN -->
    <u-navbar
      back-icon-name=""
      :background="{ background: '#d83d34' }"
      height="0"
      class="navbar"
      z-index="0"
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
      <swiper-item class="mine">
        <scroll-view scroll-y style="height: 100%;width: 100%;" class="swiper-item"><mine></mine></scroll-view>
      </swiper-item>
      <!-- 发现页面 -->
      <swiper-item class="find">
        <scroll-view
          scroll-y
          style="height: 100%; width: 100%"
          class="swiper-item"
        >
          <!-- banner -->
          <swiper
            indicator-dots
            circular
            :autoplay="autoPlay"
            :interval="5000"
            :duration="800"
          >
            <swiper-item
              v-for="item in banner"
              :key="item.id"
              class="banner-item"
              @click="clickBanner(item)"
            >
              <image
                lazy-load
                :src="item.imageUrl"
                style="width: 93%; height: 100%; border-radius: 8px"
              ></image>
              <view
                :class="item.titleColor === 'red' ? 'typeTitle1' : 'typeTitle2'"
                >{{ item.typeTitle }}</view
              >
            </swiper-item>
          </swiper>
          <!-- nav导航 -->
          <view class="nav">
            <view
              class="nav-item"
              v-for="(item, index) in navList"
              :key="index"
              @click="goNavNewPage(item.text)"
            >
              <view>
                <text :class="item.icon"></text>
                <text class="nav-item-date" v-if="item.text === '每日推荐'">{{
                  getDay
                }}</text>
              </view>
              <text>{{ item.text }}</text>
            </view>
          </view>
          <!-- 推荐歌单 -->
          <view class="recommend">
            <view class="recommend-head">
              <text class="head-title">推荐歌单</text>
              <view
                class="head-btn"
                @touchstart="isHeadBtn = true"
                @touchend="isHeadBtn = false"
                :style="
                  isHeadBtn === true ? 'background-color:rgba(0,0,0,.1)' : ''
                "
                @click="goSongListPage"
              >
                <text style="margin-left: 30rpx;">更多</text>
                <text class="iconfont icon-right-arrow" style="font-size: 26rpx; width: 0rpx;"></text>
              </view>
            </view>
            <view class="recommend-list">
              <scroll-view class="recommend-scroll" scroll-x>
                <view
                  class="imgBox"
                  v-for="item in recommendList"
                  :key="item.id"
                  @click="goPlaylistDetail(item.id)"
                >
                  <u-image
                    :src="item.picUrl"
                    mode="widthFix"
                    width="100%"
                    border-radius="7px"
                  ></u-image>
                  <view class="playNumber">
                    <text
                      class="iconfont icon-bofang"
                      style="font-size: 24rpx"
                    ></text>
                    {{ isPlayCount(item.playCount) }}
                  </view>
                  <view class="recommendSong-title">
                    <text>{{ item.name }}</text>
                  </view>
                </view>
              </scroll-view>
            </view>
          </view>
          <!-- 新歌 -->
          <view class="newSong">
            <view class="newSong-head">
              <text class="newSong-title">新歌</text>
              <view class="newSong-btn" hover-class="click-bg" hover-stay-time="200" @click="goNewsongPage">
                <text style="margin-left: 30rpx;">更多</text>
                <text class="iconfont icon-right-arrow" style="font-size: 26rpx; width: 0rpx;"></text>
              </view>
            </view>
            <music-list class="newSong-list" :currentList="newSongList"></music-list>
          </view>
          <!-- banner 背景的红色 -->
          <view class="bColorBox"></view>
        </scroll-view>
      </swiper-item>
      <!-- 电台 -->
      <swiper-item> 电台 </swiper-item>
    </swiper>
    <play-music></play-music>
  </view>
</template>

<script>
import { recommendSongList, banner, recommendNewSong, songInfo } from "@/api/platform.js";
import { mapState, mapMutations, mapGetters } from 'vuex';
import playMusic from '@/my-components/playMusic.vue';
import musicList from './components/musicList.vue';
import mine from '@/pages/mine/mine.vue';
import { handleSingerName } from '@/utils/songUtil.js';

export default {
  components: {
    playMusic, musicList, mine
  },
  data() {
    return {
      isLogIn: false,
      tabsSwiper: [{ name: "我的" }, { name: "发现" }, { name: "电台" }],
      current: 1,
      swiperCurrent: 1,
      //轮播图
      banner: [],
      //nav
      navList: [
        {
          icon: "iconfont icon-recommend",
          text: "每日推荐",
        },
        {
          icon: "iconfont icon-songList",
          text: "歌单",
        },
        {
          icon: "iconfont icon-ranking",
          text: "排行榜",
        },
      ],
      // 判断查看更多按钮被点击了
      isHeadBtn: false,
      // 推荐歌单列表
      recommendList: [],
      // 新歌列表
      newSongList: [],
      //新歌列表背景控制
      newSongBg: null
    };
  },
  onLoad() {
    this.getBanner();
    this.getRecommendList();
    this.getRecommendNewSongList();
    if(this.loginFlag){
        this.updateUserSonglist();
    }
  },
  computed: {
    ...mapState(['playlist','playingIndex']),
    ...mapGetters(['loginFlag']),
    // banner图是否自动播放
    autoPlay() {
      return this.swiperCurrent == 1;
    },
    // 获得现在是几日
    getDay() {
      return new Date().getDate();
    },
    // 处理歌手名字
		songSinger() {
			return (song) => {
                return handleSingerName(song);
            }
		}
  },
  methods: {
    ...mapMutations(['addAndPlay','setPlayingIndex','setPlayList','updateUserSonglist']),
    //获取banner
    getBanner() {
      banner().then((data) => {
        this.banner = data.banners;
      });
    },
    //获取推荐歌单
    getRecommendList() {
      recommendSongList({ musicPlatform: "1" }).then((data) => {
        // 随机获取6个不重复的推荐歌单
        let recommend = data;
        while (this.recommendList.length < 6) {
          let i = Math.floor(Math.random() * recommend.length);
          if (this.recommendList.indexOf(recommend[i]) < 0) {
            this.recommendList.push(recommend[i]);
          }
        }
      });
    },
    //获取推荐歌曲
    getRecommendNewSongList() {
      recommendNewSong({ limit: 9 }).then((data) => {
        this.newSongList = data;
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
    // 跳转新页面
    goNavNewPage(text) {
      if (text == "歌单") {
        return this.goSongListPage();
      } else if(text == '排行榜') {
        return this.goRankListPage();
      }
    },
    // 跳转歌单页面
    goSongListPage() {
      uni.navigateTo({
        url: `../songlist/songlist`,
      });
    },
    // 跳转排行榜页面
    goRankListPage() {
      uni.navigateTo({
        url: `../ranklist/ranklist`,
      });
    },
    // 跳转新歌页面
    goNewsongPage() {
      uni.navigateTo({
        url: `../newsong/newsong`,
      });
    },
    // 跳转搜索页面
    goSearch() {
      uni.navigateTo({
        url: `../search/search`,
      });
    },
    // 跳转歌单详情页
    goPlaylistDetail(id) {
      uni.navigateTo({
        url: `../songlist/songlistDetail/songlistDetail?id=${id}&musicPlatform=1`,
      });
    },
    // 处理播放数
    isPlayCount(count) {
      return count > 100000 ? (count / 10000).toFixed() + "万" : count;
    },
    // 切换新歌列表背景,得到bg index
    newTouchstart(index) {
      this.newSongBg = index;
    },
    // 添加并播放
    addToPlay(item, i) {
      this.addAndPlay(item);
    },
    //播放全部新歌
    playNewSong() {
      let cloneNewSongList = this.$u.deepClone(this.newSongList);
      this.setPlayList({list: cloneNewSongList, i: 0})
    },
    //判断新歌列表中歌曲是否是当前播放歌曲
    isPlayingSong(id) {
      if(this.playingIndex == null){
        return false;
      }
      return this.playlist[this.playingIndex].id == id;
    },
    clickBanner(item) {
      if(item.targetType == 10) {
        this.goPlaylistDetail(item.targetId);
      } else if(item.targetType == 1) {
        songInfo({ songIds:item.targetId, musicPlatform: "1"}).then(data => {
          this.addAndPlay(data[0]);
        })
      }
    }
  }
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
  z-index: 0;
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
.banner-item {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  .typeTitle1 {
    position: absolute;
    bottom: 0;
    right: 28rpx;
    color: #fff;
    font-size: 24rpx;
    padding: 10rpx 20rpx;
    // font-weight: 500;
    border-radius: 8px 0 8px 0;
    background-color: rgba($color: $bColor, $alpha: 0.9);
  }
  .typeTitle2 {
    position: absolute;
    bottom: 0;
    right: 28rpx;
    color: #fff;
    font-size: 24rpx;
    padding: 10rpx 20rpx;
    border-radius: 8px 0 8px 0;
    background-color: rgba($color: #4b8bcb, $alpha: 0.9);
  }
}
.nav {
  position: relative;
  display: flex;
  margin: 16rpx 0 32rpx 0;
  justify-content: space-around;
  .nav-item {
    display: flex;
    flex-flow: column;
    justify-content: space-around;
    align-items: center;
    width: 140rpx;
    height: 132rpx;
    // text-align: center;
    text {
      font-size: 24rpx;
    }
    view {
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      width: 90rpx;
      height: 90rpx;
      border-radius: 50%;
      background-color: $bColor;
      .iconfont {
        font-size: 52rpx;
        color: #fff;
      }
      .nav-item-date {
        margin-top: 3px;
        position: absolute;
        // top: 0;
        font-size: 24rpx;
        color: $bColor;
        font-weight: 800;
        z-index: 9;
      }
    }
  }
}
.recommend {
  display: flex;
  flex-flow: column;
  justify-content: space-between;
  align-items: center;
  height: 360rpx;
  margin-bottom: 32rpx;
  .recommend-head {
    width: 697.5rpx;
    .head-title {
      float: left;
      font-size: 38rpx;
      font-weight: 800;
    }
    .head-btn {
      float: right;
      width: 130rpx;
      height: 50rpx;
      text-align: center;
      line-height: 46rpx;
      font-size: 24rpx;
      color: #555;
      border: 1px solid #ccc;
      border-radius: 25px;
      display: flex;
    }
  }
  .recommend-list {
    width: 100%;
    .recommend-scroll {
      width: 100%;
      white-space: nowrap;
      position: relative;
      .imgBox {
        margin-left: 26.5rpx;
        position: relative;
        display: inline-block;
        width: 208rpx;
        .playNumber {
          padding: 4rpx 10rpx;
          position: absolute;
          top: 0;
          right: 0;
          font-size: 24rpx;
          color: #fff;
        }
      }
      .imgBox:last-child {
        margin-right: 26.5rpx;
      }
      .recommendSong-title {
        position: relative;
        width: 100%;
        height: 68rpx;
        font-size: 24rpx;
        overflow: hidden;
        white-space: normal;
        word-wrap: break-word;
        word-break: break-all; /* break-all(允许在单词内换行。) */
        text-overflow: ellipsis; /* 超出部分省略号 */
        display: -webkit-box; /** 对象作为伸缩盒子模型显示 **/
        -webkit-box-orient: vertical; /** 设置或检索伸缩盒对象的子元素的排列方式 **/
        -webkit-line-clamp: 2; /** 显示的行数 **/
      }
    }
  }
}
.newSong {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 400rpx;
  .newSong-head {
    width: 697.5rpx;
    margin-bottom: 10rpx;
    .newSong-title {
      font-size: 34rpx;
      font-weight: 800;
    }
    .newSong-btn {
      float: right;
      width: 130rpx;
      height: 50rpx;
      text-align: center;
      line-height: 46rpx;
      font-size: 24rpx;
      color: #555;
      border: 1px solid #ccc;
      border-radius: 25px;
      display: flex;

    }
  }
  .newSong-list {
    width: 100%;
    display: flex;
    margin-left: 50rpx;
  }
}
.bColorBox {
  position: absolute;
  top: -2px;
  width: 100%;
  height: 100px;
  background-color: $bColor;
  z-index: -1;
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
.block-wrapper {
  margin: 20px 0;

  &:last-of-type {
    margin-bottom: 0;
  }
}
.click-bg {
	background-color:rgba(0,0,0,.1);
}
</style>

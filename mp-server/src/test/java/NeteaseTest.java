import cn.hutool.json.JSONUtil;
import com.happycoding.music.MusicServerApplication;
import com.happycoding.music.service.impl.NeteaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 15:51
 */
@SpringBootTest(classes = MusicServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class NeteaseTest {

    @Autowired
    private NeteaseService neteaseService;

    //新歌速递
    @Test
    public void topSongTest(){
        Object dict = neteaseService.topSong("0",90, 0, true);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌单
    @Test
    public void topPlayListTest(){
        Object dict = neteaseService.topPlayList("全部","new",20,0, true);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //精品歌单
    @Test
    public void topPlayListHighqualityTest(){
        Object dict = neteaseService.topPlayListHighquality("全部",20,0, true);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌单详情
    @Test
    public void topPlayListDetailTest(){
        Object dict = neteaseService.playListDetail("126995075");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌曲详情
    @Test
    public void songDetailTest(){
        Object dict = neteaseService.songInfo("31140522");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌曲url
    @Test
    public void songUrlTest(){
        Object dict = neteaseService.songUrl("28978321");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //搜索
    @Test
    public void cloudSearchTest(){
        Object dict = neteaseService.cloudSearch("大地", "1", 30, 29, true);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //热搜列表(详细)
    @Test
    public void searchHotDetailTest(){
        Object dict = neteaseService.searchHotDetail();
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //搜索建议
    @Test
    public void searchSuggestTest(){
        Object dict = neteaseService.searchSuggest("天","");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //多类型搜索
    @Test
    public void searchMultiMatchTest(){
        Object dict = neteaseService.searchMultiMatch("情人","1");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌词
    @Test
    public void lyricTest(){
        Object dict = neteaseService.lyric("5249737");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //推荐歌单
    @Test
    public void personalizedTest(){
        Object dict = neteaseService.personalized(10);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //精品歌单
    @Test
    public void highQualityListTest(){
        Object dict = neteaseService.highQualityList("全部", 50, 0);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //分类歌单
    @Test
    public void categoryListTest(){
        Object dict = neteaseService.categoryList("全部", "hot",50, 0);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //所有榜单介绍
    @Test
    public void topListInfoTest(){
        Object dict = neteaseService.topListInfo();
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //所有榜单摘要
    @Test
    public void topListDetailTest(){
        Object dict = neteaseService.topListDetail();
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌手榜
    @Test
    public void topArtistListTest(){
        Object dict = neteaseService.topArtistList("1",50, 0);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //热门歌手
    @Test
    public void hotArtistListTest(){
        Object dict = neteaseService.hotArtistList(50, 0);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //新专辑列表
    @Test
    public void topAlbumListTest(){
        Object dict = neteaseService.topAlbumList("ALL", "new",1,0,2021,5);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //推荐新歌
    @Test
    public void personalizedNewSongTest(){
        Object dict = neteaseService.personalizedNewSong(10, 0);
        System.out.println(JSONUtil.toJsonStr(dict));
    }
}

import cn.hutool.json.JSONUtil;
import com.happycoding.music.MusicServerApplication;
import com.happycoding.music.service.NeteaseService;
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
        Object dict = neteaseService.topSong(0,100, 0, true);
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
        Object dict = neteaseService.playListDetail("6732079402");
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
        Object dict = neteaseService.cloudSearch("一天 许巍", "1", 30, 0, true);
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
}

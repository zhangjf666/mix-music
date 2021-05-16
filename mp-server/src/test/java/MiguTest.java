import cn.hutool.json.JSONUtil;
import com.happycoding.music.MusicServerApplication;
import com.happycoding.music.service.MiguService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/13 14:30
 */
@SpringBootTest(classes = MusicServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MiguTest {

    @Autowired
    private MiguService service;

    //推荐歌单
    @Test
    public void personalizedTest(){
        Object dict = service.personalized(1,30, "1");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌单信息
    @Test
    public void playListTest(){
        Object dict = service.playList("179730639");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌单中歌曲信息
    @Test
    public void playListSongsTest(){
        Object dict = service.playListDetails("179730639", 30);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //获取歌词
    @Test
    public void lyricTest(){
        Object dict = service.lyric("6005663ENDM");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //搜索
    @Test
    public void searchTest(){
        Object dict = service.search("吻别", 2, 30, 1);
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌曲信息
    @Test
    public void songTest(){
        Object dict = service.song("6005663ENDM");
        System.out.println(JSONUtil.toJsonStr(dict));
    }

    //歌曲url
    @Test
    public void songUrlTest(){
        Object dict = service.songUrl("6005663ENDM", "HQ");
        System.out.println(JSONUtil.toJsonStr(dict));
    }
}

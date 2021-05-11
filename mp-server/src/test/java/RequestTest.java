import cn.hutool.core.lang.Dict;
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
public class RequestTest {

    @Autowired
    private NeteaseService neteaseService;

    @Test
    public void getTopSongTest(){
        Dict dict = neteaseService.getTopSong(0,100, 0, true);
//        Dict dict = neteaseService.getHotSearchList();
        System.out.println(JSONUtil.toJsonStr(dict));
    }
}

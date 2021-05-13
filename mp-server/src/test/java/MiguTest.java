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
        Object dict = service.personalized(1,100, "1");
        System.out.println(JSONUtil.toJsonStr(dict));
    }
}

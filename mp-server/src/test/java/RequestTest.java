import com.happycoding.music.util.NeteaseRequestUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/5/10 15:51
 */
@SpringBootTest
public class RequestTest {

    @Test
    public void getTest(){
        String res = NeteaseRequestUtil.testRequest("123423");
        System.out.println(res);
    }
}

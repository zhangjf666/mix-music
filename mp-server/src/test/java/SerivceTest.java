import com.alibaba.fastjson.JSON;
import com.happycoding.music.MusicServerApplication;
import com.happycoding.music.dto.SongUrlDto;
import com.happycoding.music.entity.Role;
import com.happycoding.music.entity.User;
import com.happycoding.music.entity.UserRole;
import com.happycoding.music.service.RoleService;
import com.happycoding.music.service.SongService;
import com.happycoding.music.service.UserRoleService;
import com.happycoding.music.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: zjf
 * @Email: junfeng1987@163.com
 * @Description:
 * @Date: 2021/6/4 8:28
 */
@SpringBootTest(classes = MusicServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SerivceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SongService songService;

    @Test
    public void userServiceTest() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("$2a$10$iEpGPe7J2Zj23juaZVCi7.XR/GlzuK3IDkzd8SGPSkPn64xUNvOsG");
        user.setNickName("超级管理员");
        user.setGender("1");
        user.setType("1");
        userService.save(user);

        Role role1 = new Role();
        role1.setName("超级管理员");
        role1.setDescription("所有权限");
        roleService.save(role1);

        Role role2 = new Role();
        role2.setName("普通用户");
        role2.setDescription("普通权限");
        roleService.save(role2);

        UserRole userRole = new UserRole(user.getId(), role1.getId());
        userRoleService.save(userRole);
    }

    @Test
    public void songUrlTest(){
        SongUrlDto songUrlDto = songService.queryUrl("1400776668354297857");
        System.out.println(JSON.toJSONString(songUrlDto));
    }

    @Test
    public void songLyricTest() {
        String lyric = songService.queryLyric("1400776668354297857");
        System.out.println(lyric);
    }
}

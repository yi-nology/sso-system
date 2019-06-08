package com.murphyyi.sso.Mapper;

import com.murphyyi.sso.api.model.User;
import com.murphyyi.sso.mapper.UserMapper;
//import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: UserMapperTest
 * @description:
 * @author: zhangyi
 * @since: 2019-04-15 00:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsert() throws Exception {
//        userMapper.insert(new User("aa1", "a123456", UserSexEnum.MAN));
//        userMapper.insert(new User("bb1", "b123456", UserSexEnum.WOMAN));
//        userMapper.insert(new User("cc1", "b123456", UserSexEnum.WOMAN));

//        Assert.assertEquals(3, userMapper.getAll().size());
    }

    @Test
    public void testQuery() throws Exception {
        List<User> users = userMapper.getAll();
        System.out.println(users.toString());
    }

    @Test
    public void testUpdate() throws Exception {
        User user = userMapper.getOneByUid(171388301616349184L);
        System.out.println(user.toString());
        user.setNickName("张易");
        user.setEmail("zy84338719@hotmail.com");
        userMapper.update(user);
//        Assert.assertTrue(("张易".equals(userMapper.getOneByUid(1L).getNickName())));
    }
}
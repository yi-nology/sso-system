package com.murphyyi.sso.service.impl;

import com.alibaba.fastjson.JSON;
import com.murphyyi.sso.api.model.Result;
import com.murphyyi.sso.api.model.User;
import com.murphyyi.sso.api.model.SSOResposne;
import com.murphyyi.sso.service.TokenService;
import com.murphyyi.sso.utils.SSOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.murphyyi.sso.utils.UUIDUtils.compressUUID;
import static com.murphyyi.sso.utils.UUIDUtils.createUUID;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @ClassName: TokenService
 * @description:
 * @author: zhangyi
 * @since: 2019-03-15 16:07
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${token.default.time}")
    private int defaultTime;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * @return
     * @author: zhangyi
     * description: 生成token
     * create time: 12:55 2019-03-17
     * @Param: null
     */

    @Override
    public String generateToken(User user,
                                int expirytime) {
        if (user != null) {
            String uuid = compressUUID(createUUID());
            if (expirytime == 0) {
                expirytime = defaultTime;
            }
            redisTemplate.opsForValue().set(uuid, toJSONString(user), expirytime, SECONDS);

            log.info("用户登陆生成token={}", uuid);

            return uuid;

        } else {

//            log.error(" <=========== Invalid UserName Password combination  ===================> \n "
//                    + " userName " + userName + " password " + password);
            throw new SSOException(HttpStatus.UNAUTHORIZED.toString());

        }
    }

    /**
     * @author: zhangyi
     * @description: token 认证
     * @date : 13:18 2019-03-17
     * @Param:
     */
    @Override
    public User verificationToken(String token) {
        String userInfo = redisTemplate.opsForValue().get(token);

        if (userInfo != null) {
            return JSON.parseObject(userInfo, User.class);
        } else {
            return null;
        }

    }

    /**
    * @Description: 剔除token
    * @Param1: token
    * @return: java.lang.Boolean
    * @Author: zhangyi
    * @Date: 2019-04-18
    */
    @Override
    public Boolean rejectToken(String token) {
        return redisTemplate.opsForValue().getOperations().delete(token);
    }

    /**
    * @Description: 置换token
    * @Param1: token
    * @Param2: user
    * @Param3: expirytime
    * @return: com.murphyyi.sso.SSOResposne
    * @Author: zhangyi
    * @Date: 2019-04-18
    */
    @Override
    public SSOResposne exchange(String token, User user, int expirytime) {
        redisTemplate.opsForValue().set(token, toJSONString(user), expirytime, SECONDS);
        SSOResposne ssoResponse = new SSOResposne();
        ssoResponse.setStatus(HttpStatus.OK.value());
        ssoResponse.setToken(token);
        return ssoResponse;
    }
}

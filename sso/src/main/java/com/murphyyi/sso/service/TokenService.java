package com.murphyyi.sso.service;

import com.murphyyi.sso.api.model.Result;
import com.murphyyi.sso.api.model.User;
import com.murphyyi.sso.api.model.SSOResposne;

/**
 * @ClassName: TokenService
 * @description:
 * @author: zhangyi
 * @since: 2019-03-15 16:11
 */
public interface TokenService {
    /**
    * @Description: 生成token
    * @Param1: user
    * @Param2: expirytime
    * @return: com.murphyyi.sso.SSOResposne
    * @Author: zhangyi
    * @Date: 2019-04-18
    */
    String generateToken(User user, int expirytime);

    /**
    * @Description: token认证
    * @Param1: token
    * @return: java.lang.Object
    * @Author: zhangyi
    * @Date: 2019-04-18
    */
    User verificationToken(String token);

    /**
    * @Description: 摧毁token
    * @Param1: token
    * @return: java.lang.Boolean
    * @Author: zhangyi
    * @Date: 2019-04-18
    */
    Boolean rejectToken(String token);

    /** 
    * @Description: 置换token
    * @Param1: token
    * @Param2: user
    * @Param3: expirytime
    * @return: com.murphyyi.sso.SSOResposne
    * @Author: zhangyi
    * @Date: 2019-04-18 
    */
    SSOResposne exchange(String token, User user, int expirytime);
}

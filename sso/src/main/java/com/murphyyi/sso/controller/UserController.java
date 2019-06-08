package com.murphyyi.sso.controller;

import com.murphyyi.sso.api.model.User;
import com.murphyyi.sso.api.model.SSOResposne;
import com.murphyyi.sso.model.UserDTO;
import com.murphyyi.sso.model.UserVO;
import com.murphyyi.sso.service.TokenService;
import com.murphyyi.sso.service.UserOperation;
import com.murphyyi.sso.api.model.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.murphyyi.sso.utils.MD5Utils.randomGenerate;

/**
 * @ClassName: UserController
 * @description:
 * @author: zhangyi
 * @since: 2019-04-15 00:04
 */
@Api(value = "userController", description = "用户控制")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserOperation userOperation;

    private final TokenService tokenService;

    @Autowired
    public UserController( UserOperation userOperation, TokenService tokenService) {
        this.userOperation = userOperation;
        this.tokenService = tokenService;
    }

    /**
     * @Description: 用户注册
     * @Param1: user
     * @return: java.lang.String
     * @Author: zhangyi
     * @Date: 2019-04-18
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(@ModelAttribute UserVO userVO) {

        Boolean allowUser = userOperation.checkUser(userVO.getName());
        Boolean allowEmail = userOperation.checkEmail(userVO.getEmail());
        if (allowUser && allowEmail) {
            User user = new User();
            user.setUserName(userVO.getName());
            user.setEmail(userVO.getEmail());
            user.setPassWord(randomGenerate(userVO.getPasswd()));
            Long uid = userOperation.register(user);
            User userResult = userOperation.getUserByUid(uid);

            if (userResult != null) {
                return Result.success("","注册成功");
            }
            return Result.fail("创建失败");
        }
        return Result.fail("用户名重复");
    }

    /**
     * @Description: 用户登陆
     * @Param1: loginVO
     * @return: com.murphyyi.sso.SSOResposne
     * @Author: zhangyi
     * @Date: 2019-04-18
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public SSOResposne Login(@RequestParam("username") String username,@RequestParam("password") String password  ) {
        User user = userOperation.login(username, password);
        if (user != null) {
            return new SSOResposne(tokenService.generateToken(user, 3600),200);
        }

        return new SSOResposne();
    }

    /**
     * @Description: 用户信息更新
     * @Param1: userDTO
     * @Param2: token
     * @return: com.murphyyi.sso.SSOResposne
     * @Author: zhangyi
     * @Date: 2019-04-18
     */
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public SSOResposne updateUser(@ModelAttribute UserDTO userDTO,
                                  @RequestParam String token) {
        User user = userOperation.update(userDTO);
        log.info("用户信息更新={}", toJSONString(user));
        if (user != null) {
            return tokenService.exchange(token, user, 3600);
        }
        return new SSOResposne();
    }

    /**
     * @Description: 用户登出
     * @Param1: token
     * @return: java.lang.Boolean
     * @Author: zhangyi
     * @Date: 2019-04-18
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Object logout(@RequestParam String token) {
        log.info("登出token={}", token);
        User user = tokenService.verificationToken(token);
        log.info("登出用户信息user={}", toJSONString(user));
        Boolean flag = tokenService.rejectToken(token);
        if (flag) {
            return Result.success(flag, "token 已经清除");
        } else {
            return Result.fail("token 不存在");
        }
    }

    @RequestMapping(value = "getInfoByToken", method = RequestMethod.POST)
    public Object getUserInfo(@RequestParam String token){
        User user = tokenService.verificationToken(token);
        if(user!=null){
            return Result.success(user,"成功获取");
        }else {
            return Result.fail("获取失败");
        }
    }

}



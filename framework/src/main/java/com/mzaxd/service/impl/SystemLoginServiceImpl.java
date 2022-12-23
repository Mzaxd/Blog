package com.mzaxd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzaxd.domain.ResponseResult;
import com.mzaxd.domain.entity.LoginUser;
import com.mzaxd.domain.entity.User;
import com.mzaxd.mapper.UserMapper;
import com.mzaxd.service.LoginService;
import com.mzaxd.utils.JwtUtil;
import com.mzaxd.utils.RedisCache;
import com.mzaxd.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author root
 */
@Service
public class SystemLoginServiceImpl extends ServiceImpl<UserMapper, User>  implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        //把token封装 返回
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }
}
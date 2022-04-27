package com.design.onlineorder.config;

import com.design.onlineorder.enums.ResultEnum;
import com.design.onlineorder.service.UserService;
import com.design.onlineorder.utils.JwtUtils;
import com.design.onlineorder.utils.SpringUtils;
import com.design.onlineorder.utils.UserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Created by DrEAmSs on 2021-09-14 14:21
 */
public class UserFilter implements Filter {

    @Resource
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = httpServletRequest.getHeader("token");
        if (httpServletRequest.getRequestURI().contains("login") ||
                httpServletRequest.getRequestURI().contains("register")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        if (StringUtils.isBlank(token) && !httpServletRequest.getRequestURI().contains("preview")) {
            servletResponse.setContentType("application/json;charset=UTF-8");
            ResponseEntity<?> response = ResponseEntity.badRequest().body(ResultEnum.USER_UNVERIFIED.getLabel());
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(response));
            return;
        } else {
            // 获取token中的userid
            String userId = JwtUtils.getAudience(token);
            // 验证token
            JwtUtils.verifyToken(token, userId);
            // token验证之后设置当前用户信息
            if (Objects.isNull(UserUtils.getCurrentUser())) {
                if (Objects.isNull(userService)) {
                    userService = (UserService) SpringUtils.getBean("userServiceImpl");
                }
                UserUtils.setCurrentUser(userService.queryById(userId));
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
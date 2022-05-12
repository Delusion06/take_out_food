package com.ex.echo.filter;

import com.alibaba.fastjson.JSON;
import com.ex.echo.common.BaseContext;
import com.ex.echo.common.Constants;
import com.ex.echo.common.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    /**
     * 路径匹配器，支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        // /backend/index.html
        String requestURI = request.getRequestURI();

        log.info("拦截到请求:[{}]", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/register",
                "/user/editPassword",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求[{}]不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute(Constants.EMPLOYEE) != null) {
            log.info("用户已登录,用户id为:[{}]", request.getSession().getAttribute(Constants.EMPLOYEE));

            Long empId = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        /*
         * 移动端用户
         */
        if (request.getSession().getAttribute(Constants.USER) != null) {
            log.info("用户已登录,用户id为:[{}]", request.getSession().getAttribute(Constants.USER));

            Long userId = (Long) request.getSession().getAttribute(Constants.USER);
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(JsonModel.error(Constants.NOTLOGING)));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls       .
     * @param requestURI .
     * @return .
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}

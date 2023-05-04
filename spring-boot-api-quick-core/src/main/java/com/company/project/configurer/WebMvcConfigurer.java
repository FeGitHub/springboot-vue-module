package com.company.project.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.company.project.constant.BasicServiceMessage;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ServiceException;
import com.company.project.master.model.SysUser;
import com.company.project.service.ApiLogService;
import com.company.project.service.ErrorLogService;
import com.company.project.service.SysUserService;
import com.company.project.utils.Currents;
import com.company.project.utils.RedisUtils;
import com.company.project.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件

    @Value("${custom.token.check}")
    private boolean tokenCheck;


    @Value("${api.log.switch}")
    private boolean apiLogSwitch;//当前激活的配置文件

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private ApiLogService apiLogService;

    @Autowired
    private SysUserService sysUserService;


    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);//保留空的字段
        // 按需配置，更多参考FastJson文档哈
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(converter);
    }


    /**
     * ·
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/").addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }


    /***
     * 统一异常处理
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            //异常信息日志记录
            String appErrId = errorLogService.saveErrorLog(request, e);
            Result result = new Result();
            if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                logger.info(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在" + "," + appErrId);
            } else if (e instanceof ServletException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage() + "," + appErrId);
            } else if (e instanceof BindException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage() + "," + appErrId);
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员" + "," + appErrId);
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                logger.error(message, e);
            }
            responseResult(response, result);
            return new ModelAndView();
        });
    }


    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //日志拦截器
        if (apiLogSwitch) {
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    apiLogService.saveApiLog(request);
                    return true;
                }
            }).excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/static/**");
        }
        //接口签名认证拦截器
        if (tokenCheck) {
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    //token认证
                    Result result = checkToken(request);
                    if (result.getCode() != 200) {
                        responseResult(response, result);
                        return false;
                    }
                    return true;
                }
            }).excludePathPatterns("/comm/**", "/test/**")//公用,测试请求
                    .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
        }
    }


    /**
     * 处理封装返回参数
     *
     * @param response
     * @param result
     */
    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    /***
     * 处理登录请求
     * 及验证token是否是合法的
     * 如果合法存储下，key是token,value是操作时间
     * @param request
     * @return
     */
    private Result checkToken(HttpServletRequest request) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        //非公用请求，验证token
        String token = StringUtils.getStr(request.getHeader(Currents.TOKEN));//头部参数携带token
        String sysUserId = StringUtils.getStr(redisUtils.get(token));
        SysUser sysUser = null;
        if (!StringUtils.isEmpty(sysUserId)) {//没有对应的token信息
            sysUser = sysUserService.findById(sysUserId);
        }
        if (sysUser == null) {
            result.setCode(ResultCode.TOKEN_FAIL).setMessage(BasicServiceMessage.INVALID_LOG_INFO);
            return result;
        }
        redisUtils.set(token, sysUser.getId(), 10L, TimeUnit.MINUTES);
        return result;
    }
}

package com.company.project.web;

import com.conpany.project.Tester;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.hamcrest.Matchers;



public class CommonControllerTest  extends Tester {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }

    @Test
    public void testController() throws Exception {
             mockMvc.perform(MockMvcRequestBuilders.get("/demo/list")//请求链接
                .contentType(MediaType.APPLICATION_JSON_UTF8)//字符编码
                .param("param1", "A").param("param2", "B") //请求参数
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

    @Test
    public void version(){
        String springVersion = SpringVersion.getVersion();
        String springBootVersion = SpringBootVersion.getVersion();
        System.out.println("springVersion:"+springVersion);
        System.out.println("springBootVersion:"+springBootVersion);
    }
    @Test
    public void MD5TOKEN(){
        String TOKEN= DigestUtils.md5DigestAsHex("AD74E242CBB22602E053DA00A8C09786ADMIN".getBytes());
        System.out.println(TOKEN);//a38b4b83d97cac745529ea3dbb587b68
    }
}
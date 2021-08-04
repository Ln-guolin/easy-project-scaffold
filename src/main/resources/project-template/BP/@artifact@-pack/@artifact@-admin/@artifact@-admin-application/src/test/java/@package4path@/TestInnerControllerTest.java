package @packageName@;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = @artifact4humpCase@AdminApplication.class)
public class TestInnerControllerTest {
    @Resource
    private WebApplicationContext webApplicationContext;
    /**
     * mvc 环境对象
     */
    private MockMvc mockMvc;
    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void queryTest() throws Exception {
        RequestBuilder request;
        request = get("/inner/admin/{artifact}/queryTest")
                .header("Authorization","Bearer 302df9ee-9086-4d99-80f9-7d83dcb8ec5e")
                .header("From","Y")
                .param("name", "zhangsan");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}

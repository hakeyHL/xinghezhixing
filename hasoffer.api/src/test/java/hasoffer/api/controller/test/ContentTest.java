package hasoffer.api.controller.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by glx on 2015/10/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mvc-servlet.xml")
public class ContentTest {
    @Resource
//    private ContentController contentController;
    @Test
    public void testDeal() {
//        ModelAndView modelAndView = contentController.listDeals(1, 20);
    }

}

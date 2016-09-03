package hasoffer.test;

import hasoffer.core.user.IPriceOffNoticeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created on 2016/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class PriceOffNoticeTest {

    @Resource
    IPriceOffNoticeService priceOffNoticeService;

    @Test
    public void notice() {

        int[] idArray = {1, 2, 3, 4};

        for (int id : idArray) {
            priceOffNoticeService.priceOffCheck(id);
        }

    }

}

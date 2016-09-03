package hasoffer.core.test.cache;

import hasoffer.core.cache.SearchLogCacheManager;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.redis.ICacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Date : 2016/5/3
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class CacheTest {

    @Resource
    ICmpSkuService cmpSkuService;

    @Resource
    ICacheService cacheService;

    @Resource
    SearchLogCacheManager searchLogCacheManager;

    @Test
    public void del() {
        //2a02a62ddd62eeae23e0984d031cb6c8
        searchLogCacheManager.delCache("2a02a62ddd62eeae23e0984d031cb6c8");
    }

    @Test
    public void set() {
        cacheService.add("testexpire1", "hahaha", 5);

        System.out.println(cacheService.get("testexpire1", 0));

        try {
            TimeUnit.SECONDS.sleep(3);

        } catch (Exception e) {

        }

        System.out.println(cacheService.get("testexpire1", 5));

        try {
            TimeUnit.SECONDS.sleep(3);

        } catch (Exception e) {

        }

        System.out.println(cacheService.get("testexpire1", 5));
    }

    @Test
    public void get() {
//
        System.out.println(cacheService.get("testexpire1", 0));
    }
}

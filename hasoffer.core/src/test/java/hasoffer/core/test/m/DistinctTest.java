package hasoffer.core.test.m;

import com.mongodb.BasicDBList;
import com.mongodb.CommandResult;
import hasoffer.core.persistence.dbm.nosql.IMongoDbManager;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.product.ICmpSkuService;
import hasoffer.core.user.IDeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Date : 2016/3/30
 * Function :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-beans.xml")
public class DistinctTest {


    @Resource
    IDeviceService deviceService;
    @Resource
    IDataBaseManager dbm;
    @Resource
    IMongoDbManager mdm;
    @Resource
    ICmpSkuService cmpSkuService;
    @Resource
    MongoTemplate mongoTemplate;


    @Test
    public void testStat() {
        String jsonSql = "{distinct:'UrmDeviceEventLog', key:'deviceId'}";

        CommandResult cr = mongoTemplate.executeCommand(jsonSql);

        BasicDBList list = (BasicDBList) cr.get("values");
        String[] disv = list.toArray(new String[0]);
        System.out.println(disv.length);
    }

}


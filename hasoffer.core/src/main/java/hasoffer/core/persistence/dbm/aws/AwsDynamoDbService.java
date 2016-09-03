package hasoffer.core.persistence.dbm.aws;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.*;
import hasoffer.core.task.worker.IProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2016/1/4.
 */
public class AwsDynamoDbService {

    static DynamoDB dynamoDB = null;
    private static AwsDynamoDbService awsDynamoDbService;

    //    static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withEndpoint("http://60.205.57.57:8000");
//static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient().withEndpoint("http://192.168.1.203:8000");
    private static AmazonDynamoDBClient dynamoDBClient = getDynamoDBClient();
    private Logger logger = LoggerFactory.getLogger(AwsDynamoDbService.class);

    public static AwsDynamoDbService getInstance() {
        if (awsDynamoDbService == null) {
            awsDynamoDbService = new AwsDynamoDbService();
        }
        return awsDynamoDbService;
    }

    private static AmazonDynamoDBClient getDynamoDBClient() {
        BasicAWSCredentials basicAWSCredentials = null;

        basicAWSCredentials = new BasicAWSCredentials("AKIAI2KXGSAA6ML4ZSJQ", "vDUeGxdjPeH1ulHark/VhKlAkD4d9L/wVpBINxep");

        // todo 临时处理，日后对开发和测试环境做相应的配置
//        if (AppConfig.get(AppConfig.APP_ENV).equalsIgnoreCase("product")) {
//            basicAWSCredentials = new BasicAWSCredentials("AKIAI2KXGSAA6ML4ZSJQ", "vDUeGxdjPeH1ulHark/VhKlAkD4d9L/wVpBINxep");
//        }

        AmazonDynamoDBClient client = new AmazonDynamoDBClient(basicAWSCredentials)
                .withRegion(Regions.AP_SOUTHEAST_1);

        return client;
    }

    private static DynamoDBMapper getMapper() {
        return new DynamoDBMapper(dynamoDBClient);
    }

    public <T> T get(Class<T> clazz, Object id) {
        return getMapper().load(clazz, id);
    }

    public <T> void createTable(Class<T> clazz) {
        CreateTableRequest request = getMapper().generateCreateTableRequest(clazz);

        request.withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(100L)
                        .withWriteCapacityUnits(10L)
        );

        CreateTableResult createTableResult = dynamoDBClient.createTable(request);

        System.out.println(createTableResult.getTableDescription().toString());
    }

    public <T> void updateTable(Class<T> clazz, long readUnits, long writeUnits) {
        String tName = getTableName(clazz);

        UpdateTableResult updateTableResult = dynamoDBClient.updateTable(tName, new ProvisionedThroughput().withReadCapacityUnits(readUnits).withWriteCapacityUnits(writeUnits));
    }

    public <T> void updateTable(Class<T> clazz) {
        String tName = getTableName(clazz);

        UpdateTableRequest updateTableRequest = new UpdateTableRequest();

        dynamoDBClient.updateTable(updateTableRequest);
    }

    public <T> void deleteTable(Class<T> clazz) {
        String tName = getTableName(clazz);

        DeleteTableResult deleteTableResult = dynamoDBClient.deleteTable(tName);
    }

    public List<String> listTables() {
        ListTablesResult tables = dynamoDBClient.listTables();
        return tables.getTableNames();
    }

    public <T> String descTable(Class<T> clazz) {
        String tName = getTableName(clazz);

        return dynamoDBClient.describeTable(tName).toString();
    }

    private <T> String getTableName(Class<T> clazz) {
        String className = clazz.getName();
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public <T> void save(T t) {
        getMapper().save(t);
    }

    // 不是简单的保存整个数组，数组本身要定义为DynamoDBTable
//    public <T> void save(T... ts) {
//        getMapper().save(ts);
//    }

    private Map<String, AttributeValue> getParamMap(List<Object> params) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();

        int index = 1;
        for (Object param : params) {
            if (param instanceof String) {
                eav.put(":v" + index, new AttributeValue().withS((String) param));
            } else if (param instanceof Integer || param instanceof Long) {
                eav.put(":v" + index, new AttributeValue().withN(param.toString()));
            }

            index++;
        }

        return eav;
    }

    public <T> long count(Class<T> clazz) {

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        return getMapper().count(clazz, scanExpression);
    }

    public <T> long count(Class<T> clazz, String expressionStr, List<Object> params) {

        Map<String, AttributeValue> eav = getParamMap(params);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(expressionStr)
                .withExpressionAttributeValues(eav);

        return getMapper().count(clazz, scanExpression);
    }

    /**
     * @param clazz
     * @param expressionStr
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> scan(Class<T> clazz, String expressionStr, List<Object> params) {

        Map<String, AttributeValue> eav = getParamMap(params);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(expressionStr)
                .withExpressionAttributeValues(eav);

        ScanResultPage<T> resultPage = getMapper().scanPage(clazz, scanExpression);

        return resultPage.getResults();
    }

    public <T> boolean scanAll(Class<T> clazz, String expressionStr, List<Object> params, IProcessor process) {

        Map<String, AttributeValue> eav = getParamMap(params);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(expressionStr)
                .withExpressionAttributeValues(eav)
                .withLimit(1000);

        Map<String, AttributeValue> lastEvalKey = null;

        int page = 1;

        do {
            logger.debug("page = " + page++);
            ScanResultPage<T> resultPage = getMapper().scanPage(clazz, scanExpression);

            List<T> objs = resultPage.getResults();
            for (T t : objs) {
                process.process(t);
            }

            lastEvalKey = resultPage.getLastEvaluatedKey();
            scanExpression.withExclusiveStartKey(lastEvalKey);

        } while (lastEvalKey != null);

        return true;
    }
}

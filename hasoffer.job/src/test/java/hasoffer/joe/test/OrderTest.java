package hasoffer.joe.test;

import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.model.AffiliateOrder;
import hasoffer.base.utils.TimeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrderTest {


    @Test
    public static void main(String[] args) throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(OrderTest.class);
        Date endTime = new Date();
        //头15天
        Date startTime = TimeUtils.addDay(endTime, 0);
        List<AffiliateOrder> orderList = new ArrayList<>();
        orderList.addAll(getOrderList("affiliate357", "56e46c994b92488c91e43fad138d5c71", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("affiliate357", "56e46c994b92488c91e43fad138d5c71", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over affiliate357");
        orderList.addAll(getOrderList("xyangryrg", "c9c9b3d833054bf490c9989ac602b852", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("xyangryrg", "c9c9b3d833054bf490c9989ac602b852", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over xyangryrg");
        orderList.addAll(getOrderList("zhouxixi0", "55b1c6fa469b4e0296bb00259faf4056", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("zhouxixi0", "55b1c6fa469b4e0296bb00259faf4056", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over zhouxixi0");
        orderList.addAll(getOrderList("harveyouo", "c54bfd150ea74047a9233a4c3c3d356c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("harveyouo", "c54bfd150ea74047a9233a4c3c3d356c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over harveyouo" );
        orderList.addAll(getOrderList("allenooou", "857de2a9c43e40bfbdf572db3d728db4", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("allenooou", "857de2a9c43e40bfbdf572db3d728db4", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over allenooou" );
        orderList.addAll(getOrderList("747306881", "f3ec71e03799496d8b73c38b5456fb0b", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("747306881", "f3ec71e03799496d8b73c38b5456fb0b", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over 747306881");
        orderList.addAll(getOrderList("hlhakeygm", "01cfa560bac247eaa8a37f57fa8149f8", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("hlhakeygm", "01cfa560bac247eaa8a37f57fa8149f8", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over hlhakeygm");
        orderList.addAll(getOrderList("oliviersl", "6cf21891892d4bd8b839d85d51ac809c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("oliviersl", "6cf21891892d4bd8b839d85d51ac809c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over oliviersl");
        orderList.addAll(getOrderList("wuningSFg", "04bece2ed64945a3bce45c2f51293ef0", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAlvl(getOrderList("wuningSFg", "04bece2ed64945a3bce45c2f51293ef0", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over wuningSFg");
        //TimeUnit.SECONDS.sleep(5);
        //orderList.addAll(getOrderList("hlhakeygm", "01cfa560bac247eaa8a37f57fa8149f8", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("hlhakeygm", "01cfa560bac247eaa8a37f57fa8149f8", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over hlhakeygm: order.size="+orderList.size());
        //TimeUnit.SECONDS.sleep(5);
        //orderList.addAll(getOrderList("oliviersl", "6cf21891892d4bd8b839d85d51ac809c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("oliviersl", "6cf21891892d4bd8b839d85d51ac809c", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        //System.out.println("over oliviersl: order.size="+orderList.size());
        //TimeUnit.SECONDS.sleep(5);
        //orderList.addAll(getOrderList("wuningSFg", "04bece2ed64945a3bce45c2f51293ef0", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE, startTime, endTime));
        //orderList.addAll(getOrderList("wuningSFg", "04bece2ed64945a3bce45c2f51293ef0", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_APPROVED, startTime, endTime));
        System.out.println("over wuningSFg: order.size="+orderList.size());
        System.out.println(orderList.size());
        for(AffiliateOrder order:orderList){
            System.out.println(order.getAffID() + "_" + order.getTitle() + "_" + order.getTitle().length());
        }


    }

    private static List<AffiliateOrder> getOrderList(String affId, String token, String orderState, Date startTime, Date endTime) {
        IAffiliateProcessor<AffiliateOrder> flipProcessor = new FlipkartAffiliateProductProcessor();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Fk-Affiliate-Id", affId);
        headerMap.put("Fk-Affiliate-Token", token);
        Map<String, String> approvedParamMap = new HashMap<>();
        approvedParamMap.put(FlipkartAffiliateProductProcessor.R_START_DATE, DateFormatUtils.format(startTime, "yyyy-MM-dd"));
        approvedParamMap.put(FlipkartAffiliateProductProcessor.R_END_DATE, DateFormatUtils.format(endTime, "yyyy-MM-dd"));
        approvedParamMap.put(FlipkartAffiliateProductProcessor.R_ORDER_STATUS, orderState);
        approvedParamMap.put(FlipkartAffiliateProductProcessor.R_OFFSET, "0");
        return flipProcessor.getAffiliateOrderList(headerMap, approvedParamMap);
    }

}

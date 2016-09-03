package hasoffer.core.utils;

import hasoffer.base.model.Website;

import java.util.Random;

/**
 * Created by hs on 2016年08月11日.
 * Time 10:30
 */
public class AffliIdHelper {
    final static String[] FLIDS = new String[]{"xyangryrg", "zhouxixi0", "harveyouo", "allenooou", "747306881", "hlhakeygm", "oliviersl", "wuningSFg"};
    final static String[] SNIDS = new String[]{"89037", "104658", "82856"};
    final static String[] SHIDS = new String[]{"none", "123"};

    public static String getAffiIds() {
        Random random = new Random();
        return new StringBuilder().append(FLIDS[random.nextInt(FLIDS.length)] + ",").append(SNIDS[random.nextInt(SNIDS.length)] + ",").append(SHIDS[random.nextInt(SHIDS.length)]).toString();
    }

    public static String getAffiIdByWebsite(Website website) {
        Random random = new Random();
        switch (website.name()) {
            case "FLIPKART":
                return FLIDS[random.nextInt(FLIDS.length)];
            case "SNAPDEAL":
                return SNIDS[random.nextInt(SNIDS.length)];
            case "SHOPCLUES":
                return SHIDS[random.nextInt(SHIDS.length)];
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            String affiIds = AffliIdHelper.getAffiIdByWebsite(Website.SHOPCLUES);
            System.out.printf(affiIds);
        }
    }
}

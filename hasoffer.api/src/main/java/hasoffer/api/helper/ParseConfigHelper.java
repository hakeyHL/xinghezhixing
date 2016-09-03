package hasoffer.api.helper;

import hasoffer.api.controller.vo.ParseConfigSetting;
import hasoffer.api.controller.vo.ParseConfigVo;
import hasoffer.base.model.Website;
import hasoffer.fetch.helper.WebsiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2016/4/8
 * Function :
 */
public class ParseConfigHelper {

    public static final Website[] websites = new Website[]{
            Website.FLIPKART,
            Website.SNAPDEAL,
            Website.PAYTM,
            Website.AMAZON,
            Website.SHOPCLUES,
            Website.EBAY,
            Website.HOMESHOP18,
            Website.INDIATIMES,
            Website.MYNTRA,
            Website.JABONG,
            Website.PURPLLE
    };

    public static List<ParseConfigVo> getParseConfigs() {
        List<ParseConfigVo> configs = new ArrayList<ParseConfigVo>();
        for (Website website : websites) {
            getParseConfig(configs, website);
        }
        return configs;
    }

    private static void getParseConfig(List<ParseConfigVo> configs, Website website) {
        ParseConfigVo configVo = new ParseConfigVo();
        ParseConfigSetting parseConfigSetting = getParseSetting(website);

        configVo.setPackageName(WebsiteHelper.getPackage(website));
        configVo.setDisplayIcon(WebsiteHelper.getLogoUrl(website));
        configVo.setWebsite(website);
        configVo.setParseConfigSetting(parseConfigSetting);

        configs.add(configVo);
    }

    private static ParseConfigSetting getParseSetting(Website website) {
        ParseConfigSetting pcs = new ParseConfigSetting();

        switch (website) {
            case FLIPKART:
                pcs.setTitleResIds(new String[]{"com.flipkart.android:id/sellerName"});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{"com.flipkart.android:id/price"});
                pcs.setKey(new String[]{
                        "+Cart",
                        "BUY NOW",
                        "Share"
                });
                pcs.setFlag("word");
                break;
            case SNAPDEAL:
                pcs.setTitleResIds(new String[]{
                        "com.snapdeal.main:id/ptitleView",
                        "com.snapdeal.main:id/pdp_title"
                });
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{
                        "com.snapdeal.main:id/finalPriceTextView",
                        "com.snapdeal.main:id/automobile_booking_price"
                });
                pcs.setKey(new String[]{
                        "BOOK NOW",
                        "BUY NOW",
                        "This item is SOLD OUT"
                });
                pcs.setFlag("word");
                break;
            case EBAY:
                pcs.setTitleResIds(new String[]{
                        "com.ebay.mobile:id/item_title_textview"
                });
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{"com.ebay.mobile:id/price_textview"});
                pcs.setKey(new String[]{
                        "Buy it Now",
                        "Watch"
                });
                pcs.setFlag("word");
                break;
            case PAYTM:
                pcs.setTitleResIds(new String[]{ "net.one97.paytm:id/product_name"});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{"net.one97.paytm:id/pdp_no_offer_price"});
                pcs.setKey(new String[]{
                        "Buy for",
                        "Get for Free"
                });
                pcs.setFlag("word");
                break;
            case SHOPCLUES:
                pcs.setTitleResIds(new String[]{"com.shopclues:id/product_name"});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{"com.shopclues:id/current_price"});
                pcs.setKey(new String[]{"BUY NOW"});
                pcs.setFlag("word");
                break;
            case MYNTRA:
                pcs.setTitleResIds(new String[]{});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{});
                pcs.setKey(new String[]{});
                pcs.setFlag("");
                break;
            case HOMESHOP18:
                pcs.setTitleResIds(new String[]{});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{});
                pcs.setKey(new String[]{});
                pcs.setFlag("");
                break;
            case JABONG:
                pcs.setTitleResIds(new String[]{});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{});
                pcs.setKey(new String[]{});
                pcs.setFlag("");
                break;
            case PURPLLE:
                pcs.setTitleResIds(new String[]{});
                pcs.setSubTitleResIds(new String[]{});
                pcs.setPriceResIds(new String[]{});
                pcs.setKey(new String[]{});
                pcs.setFlag("");
                break;
        }

        return pcs;
    }

}

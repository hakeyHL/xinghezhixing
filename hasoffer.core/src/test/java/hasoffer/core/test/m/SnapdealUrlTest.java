package hasoffer.core.test.m;

import hasoffer.fetch.sites.snapdeal.SnapdealHelper;

/**
 * Date : 2016/4/13
 * Function :
 */
public class SnapdealUrlTest {

    static String[] urls = new String[]{
            "https://m.snapdeal.com/product/mhorse-one-a9-4gb-black/683246786953?aff_id=82856&utm_source=aff_prog",
            "https://m.snapdeal.com/product/sandisk-mobile-ultra-32-gb/257967?aff_id=82856&utm_source=aff_prog&ut",
            "https://m.snapdeal.com/product/lg-nexus-5x-16-gb/624068618927?aff_id=82856&utm_source=aff_prog&utm_c",
            "https://m.snapdeal.com/product/micromax-32b4500mhd-81-cm-32/640439490139?aff_id=82856&utm_source=aff",
            "https://m.snapdeal.com/product/gansta-metal-uv-protection-aviator/649384288728?aff_id=82856&utm_sour",
            "/buy/v2;jsessionid=044151FAD367AC7B83C2BF9ECFA8E56E.snapdeal?aff_sub=167753	12 Apr 2016	1        ",
            "https://m.snapdeal.com/product/sony-mdras200-in-the-ear/1285387040?aff_id=82856&utm_source=aff_prog&",
            "https://m.snapdeal.com/product/apple-iphone-6-16-gb/1270529654?aff_id=82856&utm_source=aff_prog&utm_",
            "/buy/v2?aff_sub=169590	12 Apr 2016	1                                                               ",
            "https://m.snapdeal.com/product/superx-blue-cotton-basics-skinny/667456239545?aff_id=82856&utm_source",
            "https://m.snapdeal.com/product/sony-mdrnc31em-in-the-ear/587125341?aff_id=82856&utm_source=aff_prog&",
            "https://m.snapdeal.com/product/micromax-42c0050uhd-106-cm-42/682874948797?aff_id=82856&utm_source=af",
            "https://m.snapdeal.com/product/puma-men-red-yacht-cvs/1653919851?aff_id=82856&utm_source=aff_prog&ut",
            "https://m.snapdeal.com/product/jbl-pulse-2-portable-bluetooth/630660526814?aff_id=82856&utm_source=a",
            "https://m.snapdeal.com/product/aabhi-casual-womens-short-cotton/640609863016?aff_id=82856&utm_source",
            "https://m.snapdeal.com/product/sac-tempered-glass-screen-guard/632071366135?offer_id=17&aff_id=82856",
            "/buy/v2;jsessionid=911F730D7B7D1B4C2B5B840584166BF5.snapdeal?aff_id=82856	12 Apr 2016	1           " ,
            "https://m.snapdeal.com/product/sandisk-microsdhc-card-16-gb/667863096591?aff_id=82856&utm_source=aff",
            "https://m.snapdeal.com/product/alan-jones-black-cotton-tshirt/663551361534?aff_id=82856&utm_source=a",
            "https://m.snapdeal.com/product/philips-m-power-play-qt400515/761170?aff_id=82856&utm_source=aff_prog",
            "/buy/v2?aff_timestamp=1460509822986	12 Apr 2016	1                                                ",
            "https://m.snapdeal.com/product/logitech-stereo-speaker-z120/29348?aff_id=82856&utm_source=aff_prog&u",
            "/buy/v2?aff_sub=608596	12 Apr 2016	1                                                               " ,
            "https://m.snapdeal.com/product/zaveri-pearls-golden-exquisite-floral/623352736763?aff_id=82856&utm_s",
            "https://m.snapdeal.com/product/the-house-of-tara-earthy/579995270?aff_id=82856&utm_source=aff_prog&u",
            "/buy/v2;jsessionid=47B36D1005B1FBD5DB36A26DE34DF18F.snapdeal?aff_id=82856	12 Apr 2016	1           " ,
            "https://m.snapdeal.com/product/highlander-brown-slim-fit-casual/625596584913?aff_id=82856&utm_source",
            "https://m.snapdeal.com/product/sandisk-otg32gb-32-gb-pen/629952255908?aff_id=82856&utm_source=aff_pr",
            "https://m.snapdeal.com/product/mtv-golden-wayfarer-sunglasses/676186394439?aff_id=82856&utm_source=a",
            "https://m.snapdeal.com/product/titan-leather-brown-men-regular/625993564429?aff_id=82856&utm_source=",
            "/buy/v2?aff_sub=186628	12 Apr 2016	1                                                               " ,
            "https://m.snapdeal.com/product/philips-inspa375u94-soundbar-system/644225689856?aff_id=82856&utm_sou",
            "https://m.snapdeal.com/product/fuson-premium-designer-semi-metallic/629894059498?aff_id=82856&utm_so",
            "https://m.snapdeal.com/product/apple-iphone-6-plus-16/76903694?aff_id=82856&utm_source=aff_prog&utm_"
    };

    public static void main(String[] args){
        for(String url : urls){
//            System.out.println(url);
            String id = SnapdealHelper.getProductIdByUrl(url);
            System.out.println(id);
        }

        String u = "https://m.snapdeal.com/product/favourite-bikerz-oneside-black-leatherette/676589481376?aff_id=82856&utm_source=aff_prog&utm_campaign=afts&offer_id=17&aff_sub=174675";

        System.out.println(u.substring(0,100));
    }

}

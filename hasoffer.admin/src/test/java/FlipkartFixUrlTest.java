import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.HtmlUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created on 2016/5/17.
 */
public class FlipkartFixUrlTest {

    @Test
    public void testFlipkartUrlFix() throws HttpFetchException, IOException {

        String url = "http://www.flipkart.com/pureits-leathers-genuine-slip-shoes/p/itme5f98gnbtvm4z?pid=null";

//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//
//        map.put("Accept-Language","zh-CN,zh;q=0.8");
//        map.put("Connection","keep-alive");
//        map.put("Cookie","_vz=viz_56ca7ed569cd1; FK-CMP-DATA=; __utma=19769839.1804911949.1456111312.1463456980.1463467679.112; __utmz=19769839.1463138019.104.20.utmcsr=dl.flipkart.com|utmccn=(referral)|utmcmd=referral|utmcct=/dl/levi-s-slim-fit-men-s-jeans/p/itmee6w4qgzfy8gk; VID=2.VIEAA37E6CC81847639F4629CAC146663B.1463467731.VS146345698571389457576; NSID=2.SI9A944E945BC7431EB02A25B1FFE096C2.1463467731.VIEAA37E6CC81847639F4629CAC146663B; pincode=560070; T=TI145611136000000055311883175410305224700841887561875670157882322662; _ga=GA1.2.1804911949.1456111312; s_cc=true; s_visit=1; s_ch_list=%5B%5B'Direct%2528No%2520referrer%2529'%2C'1462848579103'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463127368566'%5D%2C%5B'Affiliates'%2C'1463138014987'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463140559897'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463143787920'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463302063144'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463386743134'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463395242272'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463454611355'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463456980676'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463467656131'%5D%2C%5B'Direct%2528No%2520referrer%2529'%2C'1463470743109'%5D%5D; s_sq=%5B%5BB%5D%5D; SN=2.VIEAA37E6CC81847639F4629CAC146663B.SI9A944E945BC7431EB02A25B1FFE096C2.VS146345698571389457576.1463470749; s_ppv=17; JSESSIONID=t8fw4c7njxh3143dt3777v3tn; gpv_pn=www.flipkart.com%3Ahome; gpv_pn_t=Homepage; Direct=1");
//        map.put("Host","www.flipkart.com");
//        map.put("Upgrade-Insecure-Requests","1");
//        map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");

        HttpResponseModel response = HtmlUtils.getResponse(url, 1);

        String redirect = response.getRedirect();
    }


    @Test
    public void test1() throws IOException {

        String urlStr = "http://www.flipkart.com/barbie-bb1dgs1057-casual-shoes/p/itmeck8hztgshyhf";

        String result = "";

        URL url = new URL(urlStr);
        URLConnection con = url.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = "";
        StringBuffer buf = new StringBuffer();
        while ( (line = br.readLine()) != null ) {
            buf.append(line);
        }

        result = buf.toString();

        System.out.println(result);
    }



}

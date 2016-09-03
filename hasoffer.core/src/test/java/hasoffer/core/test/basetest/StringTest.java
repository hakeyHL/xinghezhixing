package hasoffer.core.test.basetest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.TimeUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/5/16.
 */
public class StringTest {
    @Test
    public void test() {

        String json = "{\n" +
                "    \"records\": [\n" +
                "        {\n" +
                "            \"domain\": \"hotel.elong.com \\n\\n\",\n" +
                "            \"VIP\": \"211.151.110.32\",\n" +
                "            \"aos_node\": 144\n" +
                "        },\n" +
                "        {\n" +
                "            \"domain\": \"hotel.elong.com \\n\\n\",\n" +
                "            \"VIP\": \"123.59.30.10\",\n" +
                "            \"aos_node\": 156\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(json);

        JSONArray jsonArray = jsonObject.getJSONArray("records");

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            String domain = jsonObject1.getString("domain");
            String vipCode = jsonObject1.getString("VIP");
            int aos_code = jsonObject1.getIntValue("aos_node");

            System.out.println(domain + "_" + vipCode + "_" + aos_code);
            System.out.println();

        }

    }

    @Test
    public void test12() {

        String oldUrl = "http://www.amazon.in/gp/offer-listing/B01BK92AMK";

        String newUrl = oldUrl.replace("gp/offer-listing", "dp");

        System.out.println(oldUrl);
        System.out.println(newUrl);

    }

    @Test
    public void test11() {

        String a = "&nbsp;";

        String s = StringUtils.filterAndTrim(a, Arrays.asList("&nbsp;"));

        System.out.println(s);

    }

    @Test
    public void test10() {

        String url = "{\"Transfer Speed\\\":\\\"Read 18.62 MB/sec, Write 4.02 MB/sec\\\",\\\"Part Number\\\":\\\"Cruzer Blade 8gb\\\",\\\"Color\\\":\\\"Multicolor\\\",\\\"Dimensions\\\":\\\"7.4 mm x 17.6 mm x 41.5 mm\\\",\\\"Encryption\\\":\\\"128-bit AES Encryption\\\",\\\"Weight\\\":\\\"2.50 g\\\",\\\"Brand\\\":\\\"SanDisk\\\",\\\"USB on the go\\\":\\\"No\\\",\\\"Form Factor\\\":\\\"USB Flash Drive\\\",\\\"Type\\\":\\\"Utility Pendrive\\\",\\\"Model\\\":\\\"SDCZ50-008G-I35\\\",\\\"Features\\\":\\\"<p>Ultra Portable, Small Size</p> <p>Feather light</p> <p>Smart and stylish</p>\\\",\\\"Case Material\\\":\\\"Plastic\\\",\\\"Capacity (GB)\\\":\\\"8 GB\\\",\\\"Interface\\\":\\\"USB 2.0\\\"}";

        url = StringEscapeUtils.unescapeHtml(url);

        System.out.println(url);

    }

    @Test
    public void test9() {

        String phone = "035";

        String[] split = phone.split(",");

        System.out.println(split);

    }

    @Test
    public void testStr1() {

        String str = "/gp/slredirect/redirect.html/ref=pa_sp_atf_aps_sr_pg1_1?pl=FRWcQVS5IPm8DzL%2B9KtwLxWqh7%2BsPygdWxJ3D1q1FpSj0whtJhEJuCM%2FAsFeUYEtDq7RphcrklI%2F%0AoUZE2vGaHoXIJcoi90F30Vh6k6WUHxzxm5tYtQjb%2BbxV%2FeWTHlmIjKMc%2B%2FgIcGu31fcBXg3GDsOm%0AoCKuZL1jC1IAb2p76Jw5VzKu0L41%2FHn%2BPzcKSfD9nPq%2F6ezTAS6TkYTXOHKQL4CJ6buFWpa4bQsE%0ATtCEQYM2nZydqskrNCcUItUJcfm8EymXbVoIq68y0GfK25BJ8L475G1%2FMLUIRLnIlJm15rmg0VTA%0Ao4mPVlJ%2F1YoY0zn1stk6D88rMN4BA7fT3CwKKSVl2K2w5ioN0Vg%2BLrz9e9Dy3o456mY3P8Esrbpy%0A5Avo39ElSua9lQr%2B7mk0R8bPmKC8AesnUIpS74FD7IT9w6Dml89pJUgt2JXFPoNvKO0pxvZQzVMm%0A8%2FU%2BvOZiUHUECznSC3s6nt5Kqy83b8iP3UEamI%2B1oj9iQWT%2FTOTy0Fad3qR5%2BxsfthMMytwa4jj6%0AJ5wytoINDZBRotiHw4wCu2kYb%2BwPk8BfMOXBpiWXjUiP4KxuYU%2BxzyK8%2BkvU5hhkNtNRXQnEJ1Zy%0AJGwDc%2FZITkiD%2BnQnjAaRj8im0kfjcvXzAtG5Ea5A1x1Jy2SrrlXqX4sXWOV7P8z6JMTAPM7ofYKQ%0A8JxpFcoWhIQaue4opwt%2FOFo%2F4PQVy0Ba4fwBwYE%2FSupJV4%2BIuzIuS8Jb75V%2Bci5RmOOwuM2qdrFO%0AgHgBFTrUIDFIruYsm51ZJiR4uURfI4pCataw%2B1A1qvsu%2FrqIjJnRJSczyYH7qZdv%2BpqLU6m7zZkB%0A7vXNlDS9PnsYo0jk%2FSHzCZ41Mh89RmlpxPNwY4xis512Eanptw%2BNCXn9EIFPOHvZLYwbf6K5Fzw9%0AFhbMuwvS7F19GuWgt%2Fk54QTnF2tB%2BA3%2FWze4AX13cRw2fI7dQZIJvLzVDYjNaq7JE67XTNArGuTC%0Aj0EXtNhnLNpcvIyf49ag%2FpJWHFE0lfJLxDlPIor7%2FW6bChWCDjHLd631fpAncA32dCpQWE4Hq5W9%0Azt%2FGCI8nVyx8aWjzOTBPsRdYH1n9t4Q8zBkF1TOi%2BbS%2BF4b6k0yWiUw6YAQoRFfl%2FwJVqng3i1bA%0ABJI7jaSGKWF3Zrvv7tw1ODqdJUf5HilKFggp%2Bln9ET%2FQuHWW1NoSglYLauU2%2BprB6A%3D%3D&token=0EA3486BC3CE4C876BBBFCECAD1E15943940BE9F";

        str = StringUtils.urlDecode(str);

        System.out.println(str);

    }

    @Test
    public void testStr2() {

        String str = "Layer'r Shot Compact Explode And Impact Body Spray (Pack Of 2) Combo Set (Set of 2)";

        String str1 = HexDigestUtil.md5(StringUtils.getCleanChars(str));

        System.out.println(str1);

    }


    @Test
    public void testStr3() {

        String str = "Adraxx SM401098 Digital Speedometer (NA X6)";
        Website website = Website.FLIPKART;

        System.out.println(HexDigestUtil.md5(website.name() + StringUtils.getCleanChars(str)));
    }

    @Test
    public void testStr4() {

        String str = "545";

        System.out.println(str.toLowerCase());

    }


    @Test
    public void md5Str() {
        String str = "Luckie1985";

        String s = HexDigestUtil.md5(str);

        System.out.println(s);
    }

    @Test
    public void test5() {
        Long aLong = new Long(TimeUtils.now());
        int i = aLong.intValue();
        int i1 = i % 30;
        System.out.println();
    }

    @Test
    public void test6() {
        Long aLong = new Long(TimeUtils.now());
        long i = aLong.longValue();
        long i1 = i % 30;
        System.out.println();
    }

    @Test
    public void test7() {

        String url = "http://www.amazon.in/XOLO-Q1000s-Plus-Xolo-White/dp/B00PUCPQGQ";

        String[] substr = url.split("/dp/");

        String result = "http://www.amazon.in/gp/offer-listing/" + substr[1];
        System.out.println(substr[0]);
        System.out.println(substr[1]);
        System.out.println(result);
    }

    @Test
    public void test8() {

        Pattern pattern = Pattern.compile("[0-9,+,-]*");

        String phone = "0351-+5486468";

        Matcher matcher = pattern.matcher(phone);

        System.out.println(matcher.matches());

    }
}

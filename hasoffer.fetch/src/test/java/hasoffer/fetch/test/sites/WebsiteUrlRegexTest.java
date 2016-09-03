package hasoffer.fetch.test.sites;

import hasoffer.base.model.Website;
import hasoffer.base.utils.HexDigestUtil;
import hasoffer.base.utils.StringUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/12.
 */
public class WebsiteUrlRegexTest {

    @Test
    public void testCmpskuIndex() {
        String title = "Alcatel Flash 2(Volcanic Grey, 16 GB)";
        String sourceStr = Website.FLIPKART.name() + StringUtils.getCleanChars(title);
        String md5 = HexDigestUtil.md5(sourceStr);
        System.out.println(HexDigestUtil.md5(Website.FLIPKART.name() + title));
        System.out.println(HexDigestUtil.md5(title));
        System.out.println(HexDigestUtil.md5(StringUtils.getCleanChars(title)));
        System.out.println(HexDigestUtil.md5(sourceStr));
        System.out.println(md5);
    }

    @Test
    public void testWebsiteUrlRegex() {

        String url = "http://www.amazon.com/Woodland-Brown-Softy-Leather-Slippers/dp/B00II0NY16/ref=sr_1_4=1457751403&sr=8-4&keywords=Woodland+Slippers";

        final String str = "(https?|http):\\/\\/([A-z0-9]+[_\\-]?[A-z0-9]+\\.)*[A-z0-9]+\\-?[A-z0-9]+\\.[A-z]{2,}(\\/.*)*\\/?";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            System.out.print("true");
        } else {
            System.out.println("false");
        }

    }
}

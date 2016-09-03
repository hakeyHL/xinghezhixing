package hasoffer.core.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2016/3/31.
 */
public class RegexTest {

    @Test
    public void testRegex() {

        Pattern pattern1 = Pattern.compile(".*(action=shop)$");
        Pattern pattern2 = Pattern.compile(".*(action=shop)&.*");

        String result = "";

        String str2 = "action=shop";
        String str3 = "action=shopsdfsdf";
        String str1 = "action=shop&asdfas";

        Matcher matcher1 = pattern1.matcher(str1);
        Matcher matcher2 = pattern2.matcher(str1);

        boolean b1 = matcher1.matches();
        boolean b2 = matcher2.matches();
        if (b1 || b2) {
            result = matcher1.group(1);
        }

        System.out.println(result);

    }

}

package hasoffer.joe.test;

import hasoffer.base.utils.StringUtils;

public class KeyWordTest {

    public static void main(String[] args) {
        String keyword = "Apple iPhone 6s 64 GB US Warranty Unlocked Cellphone - Retail Packaging (Rose Gold)";
        System.out.println(StringUtils.getCleanWordString(keyword));
    }
}

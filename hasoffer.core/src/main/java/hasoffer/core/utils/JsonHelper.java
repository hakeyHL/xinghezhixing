package hasoffer.core.utils;

import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.Arrays;

/**
 * Created by hs on 2016年07月29日.
 * Time 17:19
 */
public class JsonHelper {
    public static PropertyFilter filterProperty(final String[] args) {
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object o, String s, Object o1) {
                Arrays.sort(args);
                int tt = Arrays.binarySearch(args, s);
                if (tt > 0) {
                    return false;
                }
                return true;
            }
        };
        return propertyFilter;
    }
}

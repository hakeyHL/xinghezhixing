package hasoffer.core.utils;

import org.apache.commons.beanutils.Converter;

/**
 * Created by hs on 2016/6/28.
 */
public class MyDateConverter implements Converter{
    @Override
    public Object convert(Class type, Object value) {
        try {
            String str=null;

            if (value instanceof  String) {
                str=(String)value;
            }else if(value instanceof  java.util.Date){
                str=convertTimestamp(((java.util.Date) value).getTime());
            }else  if(value instanceof Long){
                str=convertTimestamp((Long)value);
            }else{
                throw new RuntimeException();
            }
            return new java.util.Date(str);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String convertTimestamp(Long time) {
        return new java.sql.Timestamp(time).toString();
    }
}

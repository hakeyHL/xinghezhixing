package hasoffer.core.persistence.dbm.aws.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import hasoffer.base.model.Website;
import hasoffer.base.utils.StringUtils;

/**
 * Date : 2016/6/15
 * Function :
 */
public class WebsiteTypeConverter implements DynamoDBMarshaller<Website> {

    @Override
    public String marshall(Website website) {
        if (website == null) {
            return null;
        }
        return website.name();
    }

    @Override
    public Website unmarshall(Class<Website> aClass, String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return Website.valueOf(s);
    }
}

package hasoffer.core.persistence.dbm.aws.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import hasoffer.base.model.SkuStatus;
import hasoffer.base.utils.StringUtils;

/**
 * Date : 2016/6/15
 * Function :
 */
public class SkuStatusTypeConverter implements DynamoDBMarshaller<SkuStatus> {

    @Override
    public String marshall(SkuStatus skuStatus) {
        if (skuStatus == null) {
            return null;
        }
        return skuStatus.name();
    }

    @Override
    public SkuStatus unmarshall(Class<SkuStatus> aClass, String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return SkuStatus.valueOf(s);
    }
}

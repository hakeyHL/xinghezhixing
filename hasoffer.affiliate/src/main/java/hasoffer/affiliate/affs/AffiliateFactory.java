package hasoffer.affiliate.affs;

import hasoffer.affiliate.affs.amazon.AmazonAffiliateProductProcessor;
import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.affs.snapdeal.SnapdealProductProcessor;
import hasoffer.base.model.Website;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2016/3/7.
 */
public class AffiliateFactory {

    private static final Map<Website, Class<IAffiliateProcessor>> affilicateMap = new HashMap<Website, Class<IAffiliateProcessor>>();
    private static Logger logger = LoggerFactory.getLogger(AffiliateFactory.class);

    static {
        affilicateMap.put(Website.FLIPKART, (Class) FlipkartAffiliateProductProcessor.class);
        affilicateMap.put(Website.SNAPDEAL, (Class) SnapdealProductProcessor.class);
        affilicateMap.put(Website.AMAZON, (Class)AmazonAffiliateProductProcessor.class);
    }

    public static IAffiliateProcessor getAffiliateProductProcessor(Website website) {
        Class<IAffiliateProcessor> productProcessorClass = affilicateMap.get(website);
        if (productProcessorClass == null) {
            return null;
        }

        return (IAffiliateProcessor) getAffiliateCategoryInstance(productProcessorClass);
    }

    private static Object getAffiliateCategoryInstance(Class affiliateCategory) {
        String className = affiliateCategory.getName();
        try {
            Class<?> clazz = Class.forName(className);
            if (!affiliateCategory.isAssignableFrom(clazz)) {
                logger.debug("Class " + className + " should be subclass of " + affiliateCategory.getName());
                return null;
            }
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("Class " + className + " not found");
            return null;
        } catch (InstantiationException e) {
            logger.error("Class " + className + " doesn't have a default constructor");
            return null;
        } catch (IllegalAccessException e) {
            logger.error("Class " + className + " default constructor is not public accessible");
            return null;
        }
    }

}

package hasoffer.fetch.helper;

import hasoffer.base.model.Website;
import hasoffer.fetch.core.ISummaryProductProcessor;
import hasoffer.fetch.sites.amazon.AmazonSummaryProductProcessor;
import hasoffer.fetch.sites.askmebazaar.AskmebazaarSummaryProductProcessor;
import hasoffer.fetch.sites.babyoye.BabyoyeSummaryProductProcessor;
import hasoffer.fetch.sites.bagittoday.BagittodaySummaryProductProcessor;
import hasoffer.fetch.sites.cromaretail.CromaretailSummaryProductProcessor;
import hasoffer.fetch.sites.ebay.EbaySummaryProductProcessor;
import hasoffer.fetch.sites.firstcry.FirstcrySummaryProductProcessor;
import hasoffer.fetch.sites.flipkart.FlipkartSummaryProductProcessor;
import hasoffer.fetch.sites.homeshop18.Homeshop18SummaryProductProdcessor;
import hasoffer.fetch.sites.indiatimes.IndiatimesSummaryProductProcessor;
import hasoffer.fetch.sites.infibeam.InfibeamSummaryProductProcessor;
import hasoffer.fetch.sites.maniacstore.ManiacstoreSummaryProductProcessor;
import hasoffer.fetch.sites.naaptol.NaaptolSummaryProductProcessor;
import hasoffer.fetch.sites.paytm.PaytmSummaryProductProcessor;
import hasoffer.fetch.sites.purplle.PurplleSummaryProductProcessor;
import hasoffer.fetch.sites.saholic.SaholicSummaryProductProcessor;
import hasoffer.fetch.sites.shopclues.ShopCluesSummaryProductProcessor;
import hasoffer.fetch.sites.shopmonk.ShopmonkSummaryProductProcessor;
import hasoffer.fetch.sites.snapdeal.SnapdealSummaryProductProcessor;
import hasoffer.fetch.sites.syberplace.SyberplaceSummaryProductProcessor;
import hasoffer.fetch.sites.theitdepot.TheitdepotSummaryProductProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Createdon 2016/3/1.
 */
public class WebsiteSummaryProductProcessorFactory {

    public static final Map<Website, Class<ISummaryProductProcessor>> websiteSummaryProcessor = new HashMap<Website, Class<ISummaryProductProcessor>>();

    private static Logger logger = LoggerFactory.getLogger(WebsiteSummaryProductProcessorFactory.class);

    static {

        websiteSummaryProcessor.put(Website.AMAZON, (Class) AmazonSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.FLIPKART, (Class) FlipkartSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.SNAPDEAL, (Class) SnapdealSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.PAYTM, (Class) PaytmSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.SHOPCLUES, (Class) ShopCluesSummaryProductProcessor.class);

        websiteSummaryProcessor.put(Website.INDIATIMES, (Class) IndiatimesSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.INFIBEAM, (Class) InfibeamSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.SAHOLIC, (Class) SaholicSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.SYBERPLACE, (Class) SyberplaceSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.ASKMEBAZAAR, (Class) AskmebazaarSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.BABYOYE, (Class) BabyoyeSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.CROMARETAIL, (Class) CromaretailSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.EBAY, (Class) EbaySummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.FIRSTCRY, (Class) FirstcrySummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.MANIACSTORE, (Class) ManiacstoreSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.NAAPTOL, (Class) NaaptolSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.PURPLLE, (Class) PurplleSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.SHOPMONK, (Class) ShopmonkSummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.HOMESHOP18, (Class) Homeshop18SummaryProductProdcessor.class);
        websiteSummaryProcessor.put(Website.BAGITTODAY, (Class) BagittodaySummaryProductProcessor.class);
        websiteSummaryProcessor.put(Website.THEITDEPOT, (Class) TheitdepotSummaryProductProcessor.class);

    }

    public static ISummaryProductProcessor getSummaryProductProcessor(Website website) {

        if (website == null) {
            return null;
        }

        Class<ISummaryProductProcessor> priceProcessorClass = websiteSummaryProcessor.get(website);
        if (priceProcessorClass == null) {
            return null;
        }

        return (ISummaryProductProcessor) getProcessorInstance(priceProcessorClass);

    }

    private static Object getProcessorInstance(Class<ISummaryProductProcessor> priceProcessorClass) {
        String className = priceProcessorClass.getName();
        try {
            Class<?> clazz = Class.forName(className);
            if (!priceProcessorClass.isAssignableFrom(clazz)) {
                logger.debug("Class " + className + " should be subclass of " + priceProcessorClass.getName());
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

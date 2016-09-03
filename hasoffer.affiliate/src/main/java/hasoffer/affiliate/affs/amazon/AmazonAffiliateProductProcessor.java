package hasoffer.affiliate.affs.amazon;

import hasoffer.affiliate.affs.IAffiliateProcessor;
import hasoffer.affiliate.affs.amazon.model.AmazonOrder;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateCategory;
import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2016/4/13.
 */
public class AmazonAffiliateProductProcessor implements IAffiliateProcessor<AmazonOrder> {

    private static final String AWS_ACCESS_KEY_ID = "AKIAIVSZWNDPM4NI34WQ";
    private static final String ASSOCIATE_TAG = "hasoffer-21";
    private static final String AWS_SECRET_KEY = "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";
    private static final String ENDPOINT = "webservices.amazon.in";
    private static Logger logger = LoggerFactory.getLogger(AmazonAffiliateProductProcessor.class);

    @Override
    public String getAffiliateToken() throws IOException {
        return null;
    }

    @Override
    public String sendRequest(String url, Map<String, String> headerMap, Map<String, String> paramMap) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public List<AffiliateCategory> getProductDirectory() throws AffiliateAPIException, IllegalAccessException, InstantiationException, IOException {
        return null;
    }

    @Override
    public List<AffiliateProduct> getProductList(String nextUrl) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public List<String> getProductNextUrlList(String productUrl) throws AffiliateAPIException, IOException {
        return null;
    }

    @Override
    public List<AffiliateProduct> getAffiliateProductByKeyword(String keyword, int resultNum) throws AffiliateAPIException, IOException {
        return null;
    }


    @Override
    public AffiliateProduct getAffiliateProductBySourceId(String sourceId) throws AffiliateAPIException, IOException {

        //获取helper
        SignedRequestsHelper helper = null;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            logger.debug("get helper fail for [" + sourceId + "]");
        }

        //拼装请求头
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
        params.put("AssociateTag", ASSOCIATE_TAG);
        params.put("Version", "2013-08-01");
        params.put("Operation", "ItemLookup");
        params.put("Timestamp", getTimestamp());
        params.put("IdType", "ASIN");
        params.put("ItemId", sourceId);
        params.put("ResponseGroup", "Large");

        //转换请求url
        String requestUrl = helper.sign(params);

        AffiliateProduct affiliateProduct = fetch(requestUrl);
        affiliateProduct.setSourceId(sourceId);

        return affiliateProduct;
    }

    private static String getTimestamp() {
        Date date = TimeUtils.nowDate();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyy-MM-dd");
        String s1 = sdf1.format(date);
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:s+08:00");
        String s2 = sdf2.format(date);

        return s1 + "T" + s2;
    }

    private static AffiliateProduct fetch(String requestUrl) throws IOException {

        AffiliateProduct affiliateProduct = new AffiliateProduct();
        affiliateProduct.setWebsite(Website.AMAZON);

        String url = null;
        String imageUrl = null;
        float price = 0.0f;
        String title = null;


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(requestUrl);
        } catch (ParserConfigurationException e) {
            logger.debug("ParserConfigurationException");
        } catch (SAXException e) {
            logger.debug("SAXException");
        }

        NodeList errorNode = doc.getElementsByTagName("Errors");
        if (errorNode.getLength() != 0) {
            affiliateProduct.setTitle("url expired");
            affiliateProduct.setProductStatus("none");
            return affiliateProduct;
        }

        Node urlNode = doc.getElementsByTagName("DetailPageURL").item(0);
        url = urlNode.getTextContent();
        url = URLDecoder.decode(url);
        String[] subStr = url.split("\\?");
        if (subStr.length > 1) {
            url = subStr[0];
        }

        Node titleNode = doc.getElementsByTagName("ItemAttributes").item(0);
        if (titleNode.hasChildNodes()) {
            NodeList childNodes = titleNode.getChildNodes();
            for (int i = childNodes.getLength() - 1; i >= 0; i--) {
                String childNodeName = childNodes.item(i).getNodeName();
                if ("Title".equals(childNodeName)) {
                    title = childNodes.item(i).getTextContent();
                    break;
                }
            }
        }


        Node imageNode = doc.getElementsByTagName("LargeImage").item(0);
        if (imageNode != null) {
            Node imageUrlNode = imageNode.getFirstChild();
            imageUrl = imageNode.getTextContent();
        }


        NodeList priceNodeList = doc.getElementsByTagName("LowestNewPrice");
        if (priceNodeList.getLength() == 0) {
            price = 0.0f;
            affiliateProduct.setProductStatus("false");
        } else {
            Node priceNode = priceNodeList.item(0);
            Node priceStringNode = priceNode.getFirstChild();
            price = Float.parseFloat(priceStringNode.getTextContent()) / 100;
        }


        affiliateProduct.setUrl(url);
        affiliateProduct.setImageUrl(imageUrl);
        affiliateProduct.setTitle(title);
        affiliateProduct.setPrice(price);
        affiliateProduct.setProductStatus("true");

        return affiliateProduct;
    }

    @Override
    public List<AmazonOrder> getAffiliateOrderList(Map<String, String> headerMap, Map<String, String> parameterMap) {
        return null;
    }
}

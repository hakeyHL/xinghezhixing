/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
 * except in compliance with the License. A copy of the License is located at
 * <p>
 * http://aws.amazon.com/apache2.0/
 * <p>
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License.
 * <p>
 * ********************************************************************************************
 * <p>
 * Amazon Product Advertising API
 * Signed Requests Sample Code
 * <p>
 * API Version: 2009-03-31
 */

package hasoffer.affiliate.affs.amazon;

import hasoffer.affiliate.model.AffiliateProduct;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class ItemLookupSample {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAIVSZWNDPM4NI34WQ";

    private static final String ASSOCIATE_TAG = "hasoffer-21";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "JEb7L9gy6FhbzBT/DyEHD6BBmz4jTEJKQ2Oy3eT8";

    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "webservices.amazon.in";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    private static final String ITEM_ID = "B0139LBR1C";

    public static void main(String[] args) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String requestUrl = null;
        String responseString = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        Map<String, String> params = new HashMap<String, String>();

        params.put("Service", "AWSECommerceService");
        params.put("AWSAccessKeyId", AWS_ACCESS_KEY_ID);
        params.put("AssociateTag", ASSOCIATE_TAG);
        params.put("Version", "2013-08-01");
        params.put("Operation", "ItemLookup");
        params.put("Timestamp", getTimestamp());

        params.put("IdType", "ASIN");
        params.put("ItemId", ITEM_ID);
        params.put("ResponseGroup", "Large");

        requestUrl = helper.sign(params);

        AffiliateProduct affiliateProduct = fetch(requestUrl);

        System.out.println(affiliateProduct.getTitle());

    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static AffiliateProduct fetch(String requestUrl) {

        AffiliateProduct affiliateProduct = new AffiliateProduct();
        affiliateProduct.setProductStatus("true");

        String url = null;
        String imageUrl = null;
        float price = 0.0f;
        String title = null;

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            NodeList errorNode = doc.getElementsByTagName("Errors");
            if (errorNode.getLength() != 0) {
                return null;
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
                    if("Title".equals(childNodeName)){
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        affiliateProduct.setUrl(url);
        affiliateProduct.setWebsite(Website.AMAZON);
        affiliateProduct.setImageUrl(imageUrl);
        affiliateProduct.setTitle(title);
        affiliateProduct.setPrice(price);


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

}

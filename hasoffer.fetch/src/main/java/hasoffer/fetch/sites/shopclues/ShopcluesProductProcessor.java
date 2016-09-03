package hasoffer.fetch.sites.shopclues;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.model.CurrencyCode;
import hasoffer.base.model.Website;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.base.utils.http.XPathUtils;
import hasoffer.fetch.core.IProductProcessor;
import hasoffer.fetch.exception.PriceNotFoundException;
import hasoffer.fetch.model.Price;
import hasoffer.fetch.model.Product;
import hasoffer.fetch.model.ProductImage;
import hasoffer.fetch.model.Sku;
import org.apache.http.conn.ConnectTimeoutException;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopcluesProductProcessor implements IProductProcessor {

    public static void main(String[] args) throws ContentParseException, HttpFetchException, ConnectTimeoutException, XPatherException {
        ShopcluesProductProcessor shopcluesProductProcessor = new ShopcluesProductProcessor();
        String id = shopcluesProductProcessor.getProductIdByUrl("http://www.shopclues.com/salt-brown-tan-boot.html");

        Product product = shopcluesProductProcessor.parseProduct("http://www.shopclues.com/salt-brown-tan-boot.html");
        System.out.println(product);
    }

    public Price getPirce(String url) throws HttpFetchException, ContentParseException {

        TagNode root = HtmlUtils.getUrlRootTagNode(url);

        // modified on 2016年1月7日 15:04:33
        String price = XPathUtils.getSubNodeStringByXPath(root, "//div[@class='price']", new PriceNotFoundException(url));
        //Deal Price:Rs.51,999
        price = StringUtils.filterAndTrim(price, Arrays.asList("[a-zA-z:\\.,]"));
        return new Price(CurrencyCode.INR, Float.valueOf(price));
    }

    @Override
    public String getUrlByProductId(String productId) {
        return null;
    }

    @Override
    public String getProductIdByUrl(String pageUrl) {
        TagNode root = null;
        try {
            root = HtmlUtils.getUrlRootTagNode(pageUrl);
           /* List<TagNode> t1 =  XPathUtils.getSubNodesByXPath(root, "//div[@class='product-about']", null);
            List<TagNode> t2 =  XPathUtils.getSubNodesByXPath(root, "//div[@class='product-about'][1]/div[@class='name']", null);
            List<TagNode> t3 =   XPathUtils.getSubNodesByXPath(root, "//div[@class='product-about'][1]/div[@class='name']/div/span", null);
            List<TagNode> t4 =   XPathUtils.getSubNodesByXPath(root, "//div[@class='product-about'][1]/div[@class='name']/div/span/span", null);
*/
            TagNode idNode = XPathUtils.getSubNodesByXPath(root, "//div[@class='product-about'][1]/div[@class='name']/div/span/span", null).get(0);
            String text = idNode.getText().toString();
            return text.split(":")[1].trim();
        } catch (HttpFetchException e) {
            e.printStackTrace();
        } catch (ContentParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Product parseProduct(String url) throws ConnectTimeoutException, HttpFetchException, XPatherException, ContentParseException {
        TagNode root = HtmlUtils.getUrlRootTagNode(url);
        Website website = Website.SHOPCLUES;
        String productTitle = XPathUtils.getSubNodeStringByXPath(root, "//div[@class='product-about']/div[@class='name']/div/h1", null);
        String subTitle = "";
        long cmpCategoryId = 0;// �ȼ���Ŀ�е�ID
        String sourceProductId = this.getProductIdByUrl(url);
        String sourceUrl = url;
        String brandName = "";
        String model = "";
        float currentPrice = 0;
        float originalPrice = 0;
        TagNode productPricingNode = XPathUtils.getSubNodeByXPath(root, "//div[@class='product-details']/div[@class='product-pricing']", null);
        for (TagNode tagNode : productPricingNode.getChildTagList()) {
            if (org.apache.commons.lang3.StringUtils.startsWith(tagNode.getAttributeByName("id"), "old_price_update")) {
                TagNode tmp = tagNode.getChildTagList().get(2).getChildTagList().get(1).getChildTagList().get(0);
                originalPrice = Float.parseFloat(tmp.getText().toString().replace(",", "").trim());
            }

            if (org.apache.commons.lang3.StringUtils.startsWith(tagNode.getAttributeByName("class"), "price")) {
                ContentNode contentNode = (ContentNode) tagNode.getAllChildren().get(1);
                currentPrice = Float.parseFloat(contentNode.getContent().replace("Rs.", "").trim());
            }
        }
        TagNode descriptionNode = XPathUtils.getSubNodeByXPath(root, "//div[@class='product-details-list']", null);
        String description = HtmlUtils.getInnerHTML(descriptionNode);

        List<ProductImage> images = new ArrayList<ProductImage>();
        TagNode productGalleryNode = XPathUtils.getSubNodeByXPath(root, "//div[@class='product-gallery']", null);
        for (TagNode tagNode : productGalleryNode.getChildTags()) {
            if (org.apache.commons.lang.StringUtils.startsWith(tagNode.getAttributeByName("id"), "product_images")) {
                List<TagNode> imageNodes = XPathUtils.getSubNodesByXPath(tagNode, "//div[@class='slide']/a", null);
                if (imageNodes != null) {
                    for (TagNode t : imageNodes) {
                        String href = t.getAttributeByName("href");
                        images.add(new ProductImage(href, href, href));
                    }
                }
            }
        }

        LinkedHashMap<String, LinkedHashMap<String, String>> attributeMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

        TagNode content_block_featuresNode = XPathUtils.getSubNodeByXPath(root, "//div[@class='product-features-list']", null);
        List<TagNode> labelNodes = XPathUtils.getSubNodesByXPath(content_block_featuresNode, "//label", null);
        if (labelNodes != null) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            for (TagNode tagNode : labelNodes) {
                String key = tagNode.getText().toString().trim();
                String value = tagNode.getParent().getChildTagList().get(1).getText().toString().trim();
                map.put(key, value);

                if (org.apache.commons.lang.StringUtils.startsWith(key, "Brand:")) {
                    brandName = value;
                }

                if (org.apache.commons.lang.StringUtils.startsWith(key, "Model ID:")) {
                    model = value;
                }
            }

            attributeMap.put("default", map);
        }

        List<Sku> skus = new ArrayList<Sku>();
        List<String> saleAttributeNames = new ArrayList<String>();
        int reviews = 0;

        TagNode starRatingNode = XPathUtils.getSubNodeByXPath(root, "//div[@class='average-rating']/div[@class='star-rating']/label", null);
        ContentNode ratingNode = (ContentNode) starRatingNode.getAllChildren().get(1);
        String ratingText = ratingNode.getContent().toString();
        ratingText = ratingText.replace("Based on", "");
        ratingText = ratingText.replace("Ratings", "");
        ratingText = ratingText.replace("Rating", "");
        ratingText = ratingText.trim();
        reviews = Integer.parseInt(ratingText);

        Product product = new Product(website,
                productTitle,
                sourceProductId,
                sourceUrl,
                brandName,
                model,
                currentPrice,
                originalPrice,
                description,
                attributeMap,
                skus,
                saleAttributeNames,
                images,
                subTitle,
                reviews);

        return product;
    }
}

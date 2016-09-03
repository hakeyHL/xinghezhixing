package hasoffer.affiliate.model;

/**
 * Created by chevy on 2016/8/10.
 */
public class FlipkartSkuInfo {

    private String productId;

    private String[] productFamily;

    private String title;

    private FlipkartImageUrls imageUrls;

    private FlipkartPrice flipkartSellingPrice;

    private String productUrl;

    private String productBrand;

    private String modelName;//Model Name
    private String modelNum;//Model Number
    private String modelId;//Model ID

    private boolean inStock;

    private String categoryPath;

    private FlipkartAttribute attributes;

    private String desc;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String[] getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String[] productFamily) {
        this.productFamily = productFamily;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FlipkartImageUrls getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(FlipkartImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    public FlipkartPrice getFlipkartSellingPrice() {
        return flipkartSellingPrice;
    }

    public void setFlipkartSellingPrice(FlipkartPrice flipkartSellingPrice) {
        this.flipkartSellingPrice = flipkartSellingPrice;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public FlipkartAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(FlipkartAttribute attributes) {
        this.attributes = attributes;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}

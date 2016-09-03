package hasoffer.core.product.solr;

import hasoffer.data.solr.IIdentifiable;

public class ProductModel2 implements IIdentifiable<Long> {
    private Long id;

    private String title;
//    private String key0;// title 前3个单词
//    private String key1;// title word 3+

    private String tag;
    private String cateTag;

    private String brand;// 品牌
    private String model;// 型号

    private long cate1;
    private long cate2;
    private long cate3;

    private String cate1Name;
    private String cate2Name;
    private String cate3Name;

    private float minPrice;
    private float maxPrice;

    private int rating;

    private long searchCount = 0; // 搜索次数，表示商品热度

    public ProductModel2() {
    }

    public ProductModel2(Long id, String title, String tag, String cateTag,
                         String brand, String model,
                         long cate1, long cate2, long cate3,
                         String cate1Name, String cate2Name, String cate3Name,
                         float minPrice, float maxPrice,
                         int rating, long searchCount) {
        this.id = id;
        this.title = title;
        this.tag = tag;

        this.brand = brand;
        this.model = model;

        this.cate1 = cate1;
        this.cate2 = cate2;
        this.cate3 = cate3;
        this.cate1Name = cate1Name;
        this.cate2Name = cate2Name;
        this.cate3Name = cate3Name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rating = rating;
        this.searchCount = searchCount;

        this.cateTag = cateTag;
    }

//    private void initKeys() {
//        key0 = title;
//        key1 = "";
//        if (StringUtils.isEmpty(title)) {
//            return;
//        }
//        title = title.replaceAll("\\s+", " ").trim();
//        String[] ts = title.split(" ");
//        if (ts.length > 3) {
//            int index = title.indexOf(ts[2]) + ts[2].length();
//            key0 = title.substring(0, index);
//            key1 = title.substring(index + 1);
//        }
//    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getCate1() {
        return cate1;
    }

    public void setCate1(long cate1) {
        this.cate1 = cate1;
    }

    public long getCate2() {
        return cate2;
    }

    public void setCate2(long cate2) {
        this.cate2 = cate2;
    }

    public long getCate3() {
        return cate3;
    }

    public void setCate3(long cate3) {
        this.cate3 = cate3;
    }

    public String getCate1Name() {
        return cate1Name;
    }

    public void setCate1Name(String cate1Name) {
        this.cate1Name = cate1Name;
    }

    public String getCate2Name() {
        return cate2Name;
    }

    public void setCate2Name(String cate2Name) {
        this.cate2Name = cate2Name;
    }

    public String getCate3Name() {
        return cate3Name;
    }

    public void setCate3Name(String cate3Name) {
        this.cate3Name = cate3Name;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(long searchCount) {
        this.searchCount = searchCount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCateTag() {
        return cateTag;
    }

    public void setCateTag(String cateTag) {
        this.cateTag = cateTag;
    }

    @Override
    public String toString() {
        return "ProductModel2{" +
                "title='" + title + '\'' +
                ", minPrice=" + minPrice +
                ", searchCount=" + searchCount +
                ", brand='" + brand + '\'' +
                '}';
    }
}

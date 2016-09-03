package hasoffer.core.product.solr;

import hasoffer.base.utils.StringUtils;
import hasoffer.data.solr.IIdentifiable;

public class ProductModel implements IIdentifiable<Long> {
    private Long id;

    private String title;

    private long cate1;
    private long cate2;
    private long cate3;

    private long cateId;
    private String category;
    private float price;

    private String description;

    private String color;
    private String size;

    private int rating;

    private String tag;

    private String key0;// title 前3个单词
    private String key1;// title word 3+

    private long searchCount = 0; // 搜索次数，表示商品热度

    public ProductModel(Long id, String title, String tag, long cateId, String category,
                        float price, String description,
                        String color, String size, int rating,
                        long cate1, long cate2, long cate3, long searchCount) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.cateId = cateId;
        this.category = category;
        this.price = price;
        this.description = description;
        this.color = color;
        this.size = size;
        this.rating = rating;

        this.cate1 = cate1;
        this.cate2 = cate2;
        this.cate3 = cate3;

        this.searchCount = searchCount;

        initKeys();
    }

    public ProductModel() {
    }

    private void initKeys() {
        key0 = title;
        key1 = "";
        if (StringUtils.isEmpty(title)) {
            return;
        }
        title = title.replaceAll("\\s+", " ").trim();
        String[] ts = title.split(" ");
        if (ts.length > 3) {
            int index = title.indexOf(ts[2]) + ts[2].length();
            key0 = title.substring(0, index);
            key1 = title.substring(index + 1);
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCateId() {
        return cateId;
    }

    public void setCateId(long cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey0() {
        return key0;
    }

    public void setKey0(String key0) {
        this.key0 = key0;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
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

    public long getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(long searchCount) {
        this.searchCount = searchCount;
    }
}

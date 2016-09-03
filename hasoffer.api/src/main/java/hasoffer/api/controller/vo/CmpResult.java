package hasoffer.api.controller.vo;

import hasoffer.base.model.AppDisplayMode;
import hasoffer.base.model.PageableResult;

import java.util.List;

/**
 * Date : 2016/5/27
 * Function :
 */
public class CmpResult {
    List<CmpProductListVo> priceList;
    float priceOff;
    ProductVo productVo;
    PageableResult pagedComparedSkuVos;
    //the lowest price
    private float bestPrice;
    //productname
    private String name;
    //total comment number
    private Long totalRatingsNum;
    //comment star number
    private int ratingNum;
    //product specification
    private String specs;
    private String copywriting;
    private AppDisplayMode displayMode;
    private boolean std;//是否为标品
    private String image;
    private Long productId;

    public CmpResult(float priceOff, ProductVo productVo, PageableResult<ComparedSkuVo> pagedComparedSkuVos) {
        this.priceOff = priceOff;
        this.productVo = productVo;
        this.pagedComparedSkuVos = pagedComparedSkuVos;
    }

    public CmpResult() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(float priceOff) {
        this.priceOff = priceOff;
    }

    public ProductVo getProductVo() {
        return productVo;
    }

    public void setProductVo(ProductVo productVo) {
        this.productVo = productVo;
    }

    public PageableResult getPagedComparedSkuVos() {
        return pagedComparedSkuVos;
    }

    public void setPagedComparedSkuVos(PageableResult pagedComparedSkuVos) {
        this.pagedComparedSkuVos = pagedComparedSkuVos;
    }

    public float getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(float bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalRatingsNum() {
        return totalRatingsNum;
    }

    public void setTotalRatingsNum(Long totalRatingsNum) {
        this.totalRatingsNum = totalRatingsNum;
    }

    public int getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(int ratingNum) {
        this.ratingNum = ratingNum;
    }

    public List<CmpProductListVo> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<CmpProductListVo> priceList) {
        this.priceList = priceList;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getCopywriting() {
        return copywriting;
    }

    public void setCopywriting(String copywriting) {
        this.copywriting = copywriting;
    }

    public AppDisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(AppDisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public boolean isStd() {
        return std;
    }

    public void setStd(boolean std) {
        this.std = std;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

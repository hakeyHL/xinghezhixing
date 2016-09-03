package hasoffer.core.bo.system;

import hasoffer.base.enums.SearchResultSort;

import java.util.List;

/**
 * Created by hs on 2016/6/21.
 */
public class SearchCriteria {
    private int comment;
    private Long minPrice;
    private Long maxPrice;
    private String keyword;
    private String categoryId;
    private int page = 1;
    private int pageSize = 20;
    private int level;
    private int priceFrom = -1;
    private int priceTo = -1;
    private List<String> pivotFields;
    private SearchResultSort sort = SearchResultSort.RELEVANCE;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getPage() {
        return page <= 1 ? 0 : page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize <= 1 ? 20 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public SearchResultSort getSort() {
        return sort;
    }

    public void setSort(SearchResultSort sort) {
        this.sort = sort;
    }

    public List<String> getPivotFields() {
        return pivotFields;
    }

    public void setPivotFields(List<String> pivotFields) {
        this.pivotFields = pivotFields;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "comment=" + comment +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", keyword='" + keyword + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", level=" + level +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                ", pivotFields=" + pivotFields +
                ", sort=" + sort +
                '}';
    }
}

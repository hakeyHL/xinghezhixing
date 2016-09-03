package hasoffer.core.product.solr;

public enum ProductModel2SortField {

    F_RELEVANCE(""),
    F_POPULARITY("searchCount"),
    F_PRICE("minPrice");

    private String fieldName;

    ProductModel2SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

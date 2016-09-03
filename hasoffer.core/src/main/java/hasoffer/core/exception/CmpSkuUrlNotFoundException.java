package hasoffer.core.exception;

/**
 * Date : 2016/6/1
 * Function :
 */
public class CmpSkuUrlNotFoundException extends BaseException {

    private long skuId; // ptmcmpsku

    public CmpSkuUrlNotFoundException(long skuId) {
        super(String.format("CmpSkuUrlNotFoundException[%d]", skuId));
        this.skuId = skuId;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }
}

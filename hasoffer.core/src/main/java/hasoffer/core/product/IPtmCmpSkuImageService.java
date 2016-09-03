package hasoffer.core.product;

import hasoffer.core.persistence.po.ptm.PtmCmpSkuImage;

import java.util.List;

/**
 * Created on 2016/8/1.
 */
public interface IPtmCmpSkuImageService {

    void createPtmCmpSkuImage(PtmCmpSkuImage ptmCmpSkuImage);

    List<PtmCmpSkuImage> findPtmCmpSkuImages(long productId);

    void delete(long ptmcmpskuid);

    /**
     * 用于图片下载
     *
     * @param skuId
     * @return
     */
    boolean downloadPtmCmpSkuImage(long skuId);

    /**
     * 用于图片下载完成后，修改下载结果的状态
     * true，下载成功
     * false，下载失败，默认false
     *
     * @param skuid
     * @param fetchResult
     */
    void updateFetchStatus(long skuid, boolean fetchResult);
}

package hasoffer.core.product;

import hasoffer.core.persistence.po.ptm.PtmImage;

/**
 * Date : 2016/1/13
 * Function :
 */
public interface IImageService {

    /**
     * 下载图片
     */
    boolean downloadImage(PtmImage image);

    void downloadImage2(PtmImage image);

    void updatePtmProductImagePath(long ptmproductId, String imagePath);
}

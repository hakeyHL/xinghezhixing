package hasoffer.core.product.impl;

import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCmpSkuImage;
import hasoffer.core.persistence.po.ptm.updater.PtmCmpSkuImageUpdater;
import hasoffer.core.product.IPtmCmpSkuImageService;
import hasoffer.core.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/8/1.
 */
@Service
public class PtmCmpSkuImageServiceImpl implements IPtmCmpSkuImageService {

    private final String Q_APP_IMAGES_PRODUCTID = "SELECT t FROM PtmCmpSkuImage t  where t.id=?0";

    @Resource
    IDataBaseManager dbm;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPtmCmpSkuImage(PtmCmpSkuImage ptmCmpSkuImage) {
        dbm.create(ptmCmpSkuImage);
    }

    @Override
    public List<PtmCmpSkuImage> findPtmCmpSkuImages(long productId) {
        return dbm.query(Q_APP_IMAGES_PRODUCTID, Arrays.asList(productId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(long ptmcmpskuid) {
        dbm.delete(PtmCmpSkuImage.class, ptmcmpskuid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean downloadPtmCmpSkuImage(long skuId) {

        PtmCmpSkuImage ptmCmpSkuImage = dbm.get(PtmCmpSkuImage.class, skuId);
        PtmCmpSkuImageUpdater updater = new PtmCmpSkuImageUpdater(skuId);

        try {

            if (ptmCmpSkuImage.getOriImageUrl1() != null) {
                //第一个图片
                if (ptmCmpSkuImage.getImagePath1() == null) {//如果imagePath1为null，下载图片
                    String imagePath1 = ImageUtil.downloadAndUpload(ptmCmpSkuImage.getOriImageUrl1());
                    updater.getPo().setImagePath1(imagePath1);
                }

                if (ptmCmpSkuImage.getOriImageUrl2() != null) {
                    //第二个图片
                    if (ptmCmpSkuImage.getImagePath2() == null) {//如果imagePath2为null，下载图片
                        String imagePath2 = ImageUtil.downloadAndUpload(ptmCmpSkuImage.getOriImageUrl2());
                        updater.getPo().setImagePath2(imagePath2);
                    }

                    if (ptmCmpSkuImage.getOriImageUrl3() != null) {
                        //第三个图片
                        if (ptmCmpSkuImage.getImagePath3() == null) {//如果imagePath3为null，下载图片
                            String imagePath3 = ImageUtil.downloadAndUpload(ptmCmpSkuImage.getOriImageUrl3());
                            updater.getPo().setImagePath3(imagePath3);
                        }

                        if (ptmCmpSkuImage.getOriImageUrl4() != null) {
                            //第四个图片
                            if (ptmCmpSkuImage.getImagePath4() == null) {//如果imagePath4为null，下载图片
                                String imagePath4 = ImageUtil.downloadAndUpload(ptmCmpSkuImage.getOriImageUrl4());
                                updater.getPo().setImagePath4(imagePath4);
                            }
                        }
                    }
                }

                dbm.update(updater);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFetchStatus(long skuid, boolean fetchResult) {

        PtmCmpSkuImageUpdater updater = new PtmCmpSkuImageUpdater(skuid);

        updater.getPo().setFetched(fetchResult);

        dbm.update(updater);
    }

}

package hasoffer.core.product.impl;

import hasoffer.base.exception.ImageDownloadOrUploadException;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmImage;
import hasoffer.core.persistence.po.ptm.updater.PtmImageUpdater;
import hasoffer.core.product.IImageService;
import hasoffer.core.utils.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * Date : 2016/1/13
 * Function :
 */
@Service
public class ImageServiceImpl implements IImageService {
    @Resource
    IDataBaseManager dbm;

    private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public void downloadImage2(PtmImage image) {
        PtmImageUpdater ptmImageUpdater = new PtmImageUpdater(image.getId());

        try {

            String path = ImageUtil.downloadAndUpload(image.getImageUrl2());
            ptmImageUpdater.getPo().setPath2(path);

        } catch (Exception e) {
            logger.error(e.getMessage() + "\t[Image download error]\t" + image.getImageUrl2());
            ptmImageUpdater.getPo().setErrTimes(image.getErrTimes() + 1);
        } finally {
            dbm.update(ptmImageUpdater);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean downloadImage(PtmImage image) {

        PtmImageUpdater ptmImageUpdater = new PtmImageUpdater(image.getId());

        String path;

        try {
            path = ImageUtil.downloadAndUpload(image.getImageUrl());
        } catch (ImageDownloadOrUploadException e) {
            try {
                String url = image.getImageUrl();

                url = url.replaceFirst("-\\d+", "-1");
                path = ImageUtil.downloadAndUpload(image.getImageUrl());

                ptmImageUpdater.getPo().setImageUrl(url);
            } catch (Exception e2) {
                logger.error(e.getMessage() + "\t[Image download error]\t" + image.getImageUrl());
                ptmImageUpdater.getPo().setErrTimes(image.getErrTimes() + 1);
                dbm.update(ptmImageUpdater);
                return false;
            }
        }

        ptmImageUpdater.getPo().setPath(path);
        dbm.update(ptmImageUpdater);

        return true;
    }

    /*public boolean downloadImage(PtmImage image) {

        PtmImageUpdater ptmImageUpdater = new PtmImageUpdater(image.getId());

        File file = new File(FileUtils.getTempDirectoryPath() + IDUtil.uuid() + ".jpg");
        // 下载图片到本地
        try {
            //HttpUtilx.getFile(image.getImageUrl(), file);
            boolean ret = HttpUtils.getImage(image.getImageUrl(), file);
            if (!ret) {
                String url = image.getImageUrl();
                url = url.replaceFirst("-\\d+", "-1");
                ret = HttpUtils.getImage(url, file);

                ptmImageUpdater.getPo().setImageUrl2(url);
                if (!ret) {
                    throw new Exception("failed to load img");
                }
            }

            // 上传图片
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("file", file);
            String resp = HttpUtils.postAsString(CoreConfig.get(CoreConfig.IMAGE_UPLOAD_URL), params);

            Map respMap = (Map) JSON.parse(resp);
            String path = (String) respMap.get("data");

            ptmImageUpdater.getPo().setPath2(path);

            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            ptmImageUpdater.getPo().setErrTimes(image.getErrTimes() + 1);
//            ptmImageUpdater.getPo().setPath2("");
            return false;
        } finally {
            // 删除图片
            dbm.update(ptmImageUpdater);
            FileUtils.deleteQuietly(file);
        }
    }*/


    /**
     * 注意使用此方法直接对ptmproduct的图片进行更新，参数imageUrl需要输入全路径
     *
     * @param ptmimageid
     * @param imagePath
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updatePtmProductImagePath(long ptmimageid, String imagePath) {

        List<PtmImage> ptmImageList = dbm.query("SELECT t FROM PtmImage t WHERE t.productId = ?0 ", Arrays.asList(ptmimageid));

        if (ptmImageList == null || ptmImageList.size() == 0) {
            return;
        }
        //修改id最小的那个

        PtmImage ptmImage = ptmImageList.get(0);

        PtmImageUpdater updater = new PtmImageUpdater(ptmImage.getId());

        updater.getPo().setPath(imagePath);
        updater.getPo().setPath2(imagePath);

        dbm.update(updater);
    }

}

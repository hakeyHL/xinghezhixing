package hasoffer.admin.worker;

import hasoffer.base.exception.ContentParseException;
import hasoffer.base.exception.HttpFetchException;
import hasoffer.base.utils.HtmlUtils;
import hasoffer.base.utils.StringUtils;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.ptm.PtmCategory2;
import hasoffer.core.persistence.po.ptm.PtmCmpSku;
import hasoffer.core.task.worker.impl.ListProcessWorkerStatus;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static hasoffer.base.utils.http.XPathUtils.getSubNodeByXPath;
import static hasoffer.base.utils.http.XPathUtils.getSubNodesByXPath;

/**
 * Created on 2016/5/3.
 */
public class FlipkartSkuCategory2GetSaveWorker implements Runnable {

    private final String Q_CATEGORY_BYNAME = "SELECT t FROM PtmCategory2 t WHERE t.name = ?0 ";
    private ListProcessWorkerStatus<PtmCmpSku> ws;
    private IDataBaseManager dbm;

    public FlipkartSkuCategory2GetSaveWorker(IDataBaseManager dbm, ListProcessWorkerStatus<PtmCmpSku> ws) {//ICategoryService categoryService
        this.dbm = dbm;
        this.ws = ws;
//        this.categoryService = categoryService;
    }

    @Override
    public void run() {
        while (true) {

            PtmCmpSku ptmCmpSku = ws.getSdQueue().poll();

            if (ptmCmpSku == null) {

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            try {
                createCateAndGetParam(ptmCmpSku);
            } catch (Exception e) {

            }
        }
    }


    private void createCateAndGetParam(PtmCmpSku sku) throws HttpFetchException, ContentParseException {

        final String CATE_PATH = "//div[@data-tracking-id='product_breadCrumbs']/ul/li";

        TagNode root = HtmlUtils.getUrlRootTagNode(sku.getUrl());

        //获取导航栏
        List<TagNode> catePathList = getSubNodesByXPath(root, CATE_PATH, new ContentParseException("cate path not found for [" + sku.getId() + "]"));

        if (catePathList.size() == 0) {//获取当行个数为0，任务抓取失败
            this.ws.getSdQueue().add(sku);
            return;
        }

        //获取导航栏中catePath的长度，取前5位或者更小
        int cateSize = catePathList.size();
        cateSize = cateSize > 6 ? 6 : cateSize;
        if (catePathList.size() == cateSize) {
            cateSize = cateSize - 1;
        }

        List<PtmCategory2> categoryList = new ArrayList<PtmCategory2>();

        for (int i = 0; i < cateSize; i++) {

            if (i == 0) {//排除导航中的第一个home
                continue;
            }

            /*
                如果有效类目size小于等于3，取最后一个
                查询是否存在该类目，存在关联id
                如果不存在，查询是否存在父类目依次创建
             */

            try {

                TagNode pathNode = getSubNodeByXPath(catePathList.get(i), "/a", new ContentParseException("path not found"));

                //获取类目名称
                String pathString = StringUtils.filterAndTrim(pathNode.getText().toString(), null);


            } catch (ContentParseException exception) {
                break;
            }

        }

        //给sku关联类目信息
//        PtmCmpSkuUpdater updater = new PtmCmpSkuUpdater(sku.getId());
//        updater.getPo().setCategoryId(parentId);
//        dbm.update(updater);

    }
}

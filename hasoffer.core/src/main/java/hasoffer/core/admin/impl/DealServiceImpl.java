package hasoffer.core.admin.impl;

import hasoffer.base.model.PageableResult;
import hasoffer.base.model.Website;
import hasoffer.base.utils.TimeUtils;
import hasoffer.core.admin.IDealService;
import hasoffer.core.persistence.dbm.HibernateDao;
import hasoffer.core.persistence.dbm.osql.IDataBaseManager;
import hasoffer.core.persistence.po.app.AppBanner;
import hasoffer.core.persistence.po.app.AppDeal;
import hasoffer.core.product.solr.DealIndexServiceImpl;
import hasoffer.core.product.solr.DealModel;
import hasoffer.core.task.ListProcessTask;
import hasoffer.core.task.worker.ILister;
import hasoffer.core.task.worker.IProcessor;
import hasoffer.core.utils.excel.ExcelImporter;
import hasoffer.core.utils.excel.ImportCallBack;
import hasoffer.core.utils.excel.ImportConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lihongde on 2016/6/21 14:03
 */
@Service
@Transactional
public class DealServiceImpl implements IDealService {

    private static final long EXPIRE_TIME_MS = 7 * 24 * 60 * 60 * 1000;

    private static final String IMPORT_SQL = "insert into appdeal(website, title, linkUrl, expireTime, priceDescription ,description, createTime,  push ,display ,imageUrl,discount,dealCategoryId,dealClickCount) values(?,?, ?, ?, ?, ? ,?, ?, ?, ?,?,?,?)";

    private static final String Q_DEALS = "SELECT t FROM AppDeal t";

    @Resource
    IDataBaseManager dbm;
    @Resource
    DealIndexServiceImpl dealIndexService;
    @Resource
    ExcelImporter importer;
    @Resource
    private HibernateDao dao;

    public static void main(String[] args) {
        if (!(Website.valueOf("SHOP") instanceof Website)) {
            System.out.printf("hha ");
        }
    }

    @Override
    public PageableResult<AppDeal> findDealList(int page, int size) {
        return dbm.queryPage("select t from AppDeal t order by t.id desc", page, size);
    }

    @Override
    public Map<String, Object> importExcelFile(MultipartFile multipartFile) throws Exception {
        Map<String, Object> importResult = importer.setImportConfig(new ImportConfig() {
                                                                        @Override
                                                                        public String validation(Workbook xwb) {
                                                                            return null;
                                                                        }

                                                                        @Override
                                                                        public String getImportSQL() {
                                                                            return IMPORT_SQL;
                                                                        }

                                                                        @Override
                                                                        public List<Object[]> getImportData(HibernateDao dao, List<Object[]> data) {

                                                                            return data;
                                                                        }

                                                                        @Override
                                                                        public ImportCallBack getImportCallBack() {
                                                                            return new ImportCallBack() {
                                                                                @Override
                                                                                public Map<String, Object> preOperation(HibernateDao dao, List<Object[]> data) {
                                                                                    Website website = null;
                                                                                    Map<String, Object> map = new HashMap<String, Object>();
                                                                                    int _nullRows = 0;
                                                                                    int repeatRows = 0;
                                                                                    List<Object[]> dataQueue = new LinkedList<Object[]>();
                                                                                    for (int i = 0; i < data.size(); i++) {
                                                                                        Object[] tempData = new Object[13];
                                                                                        for (int j = 0; j < tempData.length; j++) {
                                                                                            //TODO  网站名/deal名称/deal跳转链接为空 记录日志
                                                                                            if (StringUtils.isBlank(data.get(i)[0] + "") || StringUtils.isBlank(data.get(i)[1] + "") || StringUtils.isBlank(data.get(i)[2] + "")) {
                                                                                                _nullRows++;
                                                                                            } else {
                                                                                                System.arraycopy(data.get(i), 0, tempData, 0, data.get(i).length);
                                                                                            }

                                                                                            if (!StringUtils.isBlank(tempData[0] + "")) {
                                                                                                tempData[0] = tempData[0].toString().toUpperCase();
                                                                                                //检查字符串是否属于规范
                                                                                                try {
                                                                                                    website = Website.valueOf(tempData[0] + "");
                                                                                                } catch (Exception e) {
                                                                                                    System.out.printf(e.getMessage());
                                                                                                    website = null;
                                                                                                }
                                                                                            }

                                                                                            if (StringUtils.isBlank(tempData[3] + "")) {
                                                                                                tempData[3] = TimeUtils.after(EXPIRE_TIME_MS);
                                                                                            }
                                                                                            if (tempData[5] == null || StringUtils.isBlank(tempData[5] + "")) {
                                                                                                StringBuilder sb = new StringBuilder();
                                                                                                sb.append(tempData[0]).append(" is offering ").append(tempData[1]).append(" .\n");
                                                                                                sb.append("\n");
                                                                                                sb.append("Steps to order the item at ").append(tempData[0]).append(" website: \n");
                                                                                                sb.append("\n");
                                                                                                sb.append("1. First, visit the offer page at ").append(tempData[0]).append(" .\n");
                                                                                                sb.append("2. Select your product according to the item variety.\n");
                                                                                                sb.append("3. Then click on Buy Now option. \n");
                                                                                                sb.append("4. Sign in/ Sign up at ").append(tempData[0]).append(" and fill up your address. \n");
                                                                                                sb.append("5. Choose your payment option and make payment your cart value.").append(" .\n");
                                                                                                tempData[5] = sb.toString();
                                                                                            }

                                                                                            if (tempData[6] == null || StringUtils.isBlank(tempData[6] + "")) {
                                                                                                tempData[6] = new Date(TimeUtils.now());
                                                                                            }

                                                                                            if (tempData[7] == null || StringUtils.isBlank(tempData[7] + "")) {
                                                                                                tempData[7] = 0;
                                                                                            }

                                                                                            if (tempData[8] == null || StringUtils.isBlank(tempData[8] + "")) {
                                                                                                tempData[8] = 0;
                                                                                            }
                                                                                            if (tempData[10] == null || StringUtils.isBlank(tempData[10] + "")) {
                                                                                                tempData[10] = 50;
                                                                                            }
                                                                                            if (tempData[11] == null || StringUtils.isBlank(tempData[11] + "")) {
                                                                                                tempData[11] = -1l;
                                                                                            }
                                                                                            if (tempData[12] == null || StringUtils.isBlank(tempData[12] + "")) {
                                                                                                tempData[12] = 0l;
                                                                                            }

                                                                                            // TODO 重复元素记录日志
                                                                                            for (int k = 0; k < dataQueue.size(); k++) {
                                                                                                if (dataQueue.get(k)[2].equals(tempData[2])) {
                                                                                                    repeatRows++;
                                                                                                    dataQueue.remove(k);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if (website != null) {
                                                                                            dataQueue.add(tempData);
                                                                                        } else {
                                                                                            continue;
                                                                                        }
                                                                                    }
                                                                                    map.put("_nullRows", _nullRows);
                                                                                    map.put("repeatRows", repeatRows);
                                                                                    map.put("dataQueue", dataQueue);
                                                                                    return map;
                                                                                }

                                                                                @Override
                                                                                public void postOperation(HibernateDao dao, List<Object[]> data) {

                                                                                }
                                                                            };

                                                                        }
                                                                    }

        ).importExcelFile(multipartFile);

        return importResult;
    }

    @Override
    public AppDeal getDealById(Long dealId) {

        return dbm.get(AppDeal.class, dealId);
    }

    @Override
    public AppBanner getBannerByDealId(Long dealId) {
        return (AppBanner) dbm.querySingle("SELECT t FROM AppBanner t WHERE t.sourceId = ?0 ", Arrays.asList(dealId.toString()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeal(Long dealId) {
        dbm.delete(AppDeal.class, dealId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBanner(Long bannerId) {
        dbm.delete(AppBanner.class, bannerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        dao.updateBySql("delete from appdeal where id in(?)", Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBanner(AppBanner banner) {
        dao.save(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeal(AppDeal deal) {
        dao.save(deal);
    }

    @Override
    public void importDeal2Solr(DealModel dm) {
        dealIndexService.createOrUpdate(dm);
    }

    @Override
    public void reimportAllDeals2Solr() {

        ListProcessTask<AppDeal> listAndProcessTask2 = new ListProcessTask<AppDeal>(
                new ILister<AppDeal>() {
                    @Override
                    public PageableResult getData(int page) {
                        return dbm.queryPage(Q_DEALS, page, 500);
                    }

                    @Override
                    public boolean isRunForever() {
                        return false;
                    }

                    @Override
                    public void setRunForever(boolean runForever) {

                    }
                },
                new IProcessor<AppDeal>() {
                    @Override
                    public void process(AppDeal o) {
                        DealModel dm = new DealModel(o);
                        importDeal2Solr(dm);
                    }
                }
        );

        try {
            dealIndexService.removeAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        listAndProcessTask2.go();
    }
}

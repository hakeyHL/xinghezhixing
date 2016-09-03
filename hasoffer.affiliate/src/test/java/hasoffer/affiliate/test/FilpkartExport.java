package hasoffer.affiliate.test;

import hasoffer.affiliate.affs.flipkart.FlipkartAffiliateProductProcessor;
import hasoffer.affiliate.exception.AffiliateAPIException;
import hasoffer.affiliate.model.AffiliateOrder;
import hasoffer.base.utils.ExcelExportUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilpkartExport {

    public static void main(String[] args) throws AffiliateAPIException, IOException {

//        String url = "https://affiliate-api.flipkart.net/affiliate/report/orders/detail/json?startDate=2016-05-25&endDate=2016-05-25&status=tentative&offset=1";
//
        FlipkartAffiliateProductProcessor processor = new FlipkartAffiliateProductProcessor();
//
//        String jsonString = processor.sendRequest(url, null);
//        Gson gson = new Gson();
//        AffiliateOrderReport report = gson.fromJson(jsonString, AffiliateOrderReport.class);
        FileOutputStream fileOut = new FileOutputStream("d:/订单报表-04.xlsx");
        XSSFWorkbook xssfWorkBook = ExcelExportUtil.createXssfWorkBook();
        XSSFSheet xssfSheet = ExcelExportUtil.createXssfSheet(xssfWorkBook, null);
        Map<Integer, ExcelExportUtil.ColumnModel> columnModelMap = new HashMap<Integer, ExcelExportUtil.ColumnModel>();
        int columnIndex = 0;
        XSSFCellStyle numericStyle = ExcelExportUtil.getNumericStyle(xssfSheet);
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("price", "price", Cell.CELL_TYPE_NUMERIC, numericStyle));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("category", "category"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("title", "title"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("productId", "productId"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("saleAmount", "saleAmount", Cell.CELL_TYPE_NUMERIC, numericStyle));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("saleCurrency", "saleCurrency"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("commissionRate", "commissionRate", Cell.CELL_TYPE_NUMERIC, numericStyle));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("tentativeAmount", "tentativeAmount", Cell.CELL_TYPE_NUMERIC, numericStyle));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("tentativeCurrency", "tentativeCurrency"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("status", "status"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("affiliateOrderItemId", "affiliateOrderItemId", Cell.CELL_TYPE_NUMERIC));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("orderDate", "orderDate"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("affExtParam1", "affExtParam1"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("deviceId", "affExtParam2"));
        columnModelMap.put(columnIndex++, new ExcelExportUtil.ColumnModel("salesChannel", "salesChannel"));
        columnModelMap.put(columnIndex, new ExcelExportUtil.ColumnModel("customerType", "customerType"));

        Map<String, String> headerMap = new HashMap<String, String>();
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("startDate", "2016-08-01");
        parameterMap.put("endDate", "2016-08-02");
        parameterMap.put("status", FlipkartAffiliateProductProcessor.R_ORDER_STATUS_TENTATIVE);
        parameterMap.put("offset", "0");
        List<AffiliateOrder> affiliateOrderList = processor.getAffiliateOrderList(headerMap, parameterMap);
        //parameterMap.put("startDate", "2016-04-01");
        //parameterMap.put("endDate", "2016-04-30");
        //parameterMap.put("status", "approved");
        //parameterMap.put("offset", "501");
        //affiliateOrderList.addAll(processor.getAffiliateOrderList(parameterMap));
        ExcelExportUtil.initXSSFSheetData(xssfSheet, columnModelMap, affiliateOrderList);
        xssfWorkBook.write(fileOut);//把Workbook对象输出到文件workbook.xls中
        fileOut.close();

    }
}

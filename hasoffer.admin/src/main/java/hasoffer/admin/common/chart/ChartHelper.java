package hasoffer.admin.common.chart;

import hasoffer.base.model.Website;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date : 2016/1/8
 * Function :
 */
public class ChartHelper {


    public static List<Chartd> getChartData(Map<Website, List<Float>> priceMap) {

        List<Chartd> chartds = new ArrayList<Chartd>();

        if (priceMap == null){
            return chartds;
        }

        for (Map.Entry<Website, List<Float>> kv : priceMap.entrySet()) {
            Website website = kv.getKey();
            chartds.add(new Chartd(website.name(), kv.getValue()));
        }
        return chartds;
    }
}

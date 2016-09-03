package hasoffer.fetch.helper;

import hasoffer.fetch.model.SkuAttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/10/30
 */
public class SkuHelper {

	/**
	 * 根据 attribute def map 生成所有匹配的sku
	 *
	 * @param skuAttributeDefMap
	 * @param currentPrice
	 * @param originalPrice
	 */
	public static List<Map<String, SkuAttributeValue>> transformSkus(Map<String, List<SkuAttributeValue>> skuAttributeDefMap,
	                                                                 List<String> saleAttributeNames) {
		int attrNameSize = saleAttributeNames.size();

		List<Map<String, SkuAttributeValue>> skuAttributes = new ArrayList<Map<String, SkuAttributeValue>>();

		// name - value : list
		for (int i = 0; i < attrNameSize; i++) {
			String attrName = saleAttributeNames.get(i);
			if (i == 0) {
				List<SkuAttributeValue> skuAttributeValues = skuAttributeDefMap.get(attrName);
				for (SkuAttributeValue skuAttributeValue : skuAttributeValues) {
					Map<String, SkuAttributeValue> skuAttributeValueMap = new HashMap<String, SkuAttributeValue>();
					skuAttributeValueMap.put(attrName, skuAttributeValue);
					skuAttributes.add(skuAttributeValueMap);
				}
				continue;
			}

			skuAttributes = transform(skuAttributes, skuAttributeDefMap.get(attrName), attrName);
		}

		return skuAttributes;
	}

	private static List<Map<String, SkuAttributeValue>> transform(List<Map<String, SkuAttributeValue>> savms,
	                                                              List<SkuAttributeValue> savs, String name) {

		List<Map<String, SkuAttributeValue>> skuAttributes = new ArrayList<Map<String, SkuAttributeValue>>();

		for (Map<String, SkuAttributeValue> isav : savms) {
			for (SkuAttributeValue sav : savs) {
				Map<String, SkuAttributeValue> targetSavm = new HashMap<String, SkuAttributeValue>();
				for (Map.Entry<String, SkuAttributeValue> kv : isav.entrySet()) {
					targetSavm.put(kv.getKey(), kv.getValue());
				}
				targetSavm.put(name, sav);
				skuAttributes.add(targetSavm);
			}
		}

		return skuAttributes;
	}

}

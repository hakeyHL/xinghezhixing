package hasoffer.core.product;

import hasoffer.fetch.sites.Ali.model.AliSpu;

/**
 * Author : CHENGWEI ZHANG
 * Date : 2015/11/21
 */
public interface IAliSpuService {

	/**
	 * 保存alispu
	 * 创建品牌、型号
	 *
	 * @param aliSpu
	 */
	void saveAliSpu(AliSpu aliSpu);

}

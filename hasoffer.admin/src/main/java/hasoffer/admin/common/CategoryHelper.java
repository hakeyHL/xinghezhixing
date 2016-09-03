package hasoffer.admin.common;

import hasoffer.admin.controller.vo.CategoryVo;
import hasoffer.base.utils.ArrayUtils;
import hasoffer.core.persistence.po.ptm.PtmCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2015/12/24.
 */
public class CategoryHelper {

	public static List<CategoryVo> getCategoryVos(List<PtmCategory> categoryList) {
		List<CategoryVo> categoryVos = new ArrayList<CategoryVo>();
		if (ArrayUtils.hasObjs(categoryList)) {
			for (PtmCategory category : categoryList) {
				categoryVos.add(new CategoryVo(category));
			}
		}

		return categoryVos;
	}

}

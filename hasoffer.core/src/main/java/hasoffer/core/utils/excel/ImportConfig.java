package hasoffer.core.utils.excel;

import java.util.List;

import hasoffer.core.persistence.dbm.HibernateDao;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 导入配置接口，提供Excel数据格式校验函数，获取导入sql函数，获取导入数据函数以及获取导入回调接口函数
 */
public interface ImportConfig {

	/**
	 * Excel数据或格式校验函数，用来判断导入Excel中的数据或者格式是否有误
	 * 
	 * @param xwb
	 *            Excel对象
	 * @return 包含错误信息的字符串
	 */
	String validation(Workbook xwb);

	/**
	 * 获取导入sql函数
	 * 
	 * @return 导入sql
	 */
	String getImportSQL();

	/**
	 * 获取导入数据
	 * 
	 * @param data
	 *            原始Excel数据，以行进行划分
	 * @return List&lt;Object[]&gt;转换后即将导入的数据
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	List<Object[]> getImportData(HibernateDao dao, List<Object[]> data);

	/**
	 * 获取导入操作回调接口
	 * 
	 * @see ImportCallBack
	 * @return {@link ImportCallBack}
	 */
	ImportCallBack getImportCallBack();
}

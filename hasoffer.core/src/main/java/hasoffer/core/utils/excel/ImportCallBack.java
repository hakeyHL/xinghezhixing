package hasoffer.core.utils.excel;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import hasoffer.core.persistence.dbm.HibernateDao;

/**
 * 导入回调借口 <br />
 * 注：目前由于@Transactional并不起作用，所以选择HibernateDao进行事务处理，后期调整
 * 
 * @author Guoyan
 */
@Transactional
public interface ImportCallBack {

	/**
	 * 导入之前需要进行的数据库操作
	 * 
	 * @param dao
	 *            {@link HibernateDao}
	 * @param data
	 *            原始Excel中的数据，按行进行分割
     * @return
	 */
	Map<String, Object> preOperation(HibernateDao dao, List<Object[]> data);

	/**
	 * 导入之后需要进行的数据库操作
	 * 
	 * @param dao
	 *            {@link HibernateDao}
	 * @param data
	 *            即将导入的数据，即进行转换后的数据
	 */
	void postOperation(HibernateDao dao, List<Object[]> data);
}

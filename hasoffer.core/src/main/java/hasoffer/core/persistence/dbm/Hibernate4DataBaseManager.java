package hasoffer.core.persistence.dbm;
import hasoffer.core.persistence.dbm.osql.AbstractHibernate4DataBaseManager;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by glx on 2015/8/25.
 */
@Component
public class Hibernate4DataBaseManager extends AbstractHibernate4DataBaseManager {
	@Resource(name = "org.springframework.orm.hibernate4.HibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	@Override
	protected HibernateTemplate getHibernate4Template() {
		return hibernateTemplate;
	}
}

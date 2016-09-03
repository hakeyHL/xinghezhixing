package hasoffer.core.persistence.dbm.osql;

import hasoffer.core.persistence.dbm.mock.Mock;
import hasoffer.core.persistence.dbm.osql.exception.OSqlException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Updater<ID extends Serializable, PO extends Identifiable<ID>> {
	private Map<String, Object> parameter = new HashMap<String, Object>();
	private PO po;
	private ID id;

	private Class<PO> poClass;


	public Updater(Class<PO> poClass, ID id) {
		this.id = id;
		this.poClass = poClass;
		po = Mock.mock(poClass, new MethodInterceptor() {
			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				String methodName = method.getName();
				String fieldName = "";

				if ("setId".equals(methodName)) {
					throw new OSqlException("此处不能setId，id为主键");
				}

				if (methodName.startsWith("set")) {
					fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
					parameter.put(fieldName, objects[0]);
				}

				return null;
			}
		});
	}

	public Class<PO> getPoClass() {
		return poClass;
	}

	public void reset() {
		parameter.clear();
		parameter.put("id", id);
	}

	public Map<String, Object> getParameter() {
		return parameter;
	}

	public PO getPo() {
		return po;
	}

	public ID getId() {
		return id;
	}
}
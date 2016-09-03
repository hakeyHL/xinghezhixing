package hasoffer.core.persistence.dbm.mock;

import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.VisibilityPredicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.ArrayList;
import java.util.List;

public class Mock {
	public static <T> T mock(Class<T> toMock, MethodInterceptor methodInterceptor) {

		Enhancer enhancer = new Enhancer() {
			@SuppressWarnings("unchecked")
			// Filter all private constructors but do not check that there are
			// some left
			protected void filterConstructors(Class sc, List constructors) {
				CollectionUtils.filter(constructors, new VisibilityPredicate(sc, true));
			}
		};

		List<Class> interfaces = new ArrayList<Class>();
		interfaces.add(IAliasable.class);
		if (toMock.isInterface()) {
			interfaces.add(toMock);
		} else {
			enhancer.setSuperclass(toMock);
		}

		enhancer.setInterfaces(interfaces.toArray(new Class[]{}));

		enhancer.setCallbackType(methodInterceptor.getClass());
		enhancer.setCallback(methodInterceptor);

		/*
		 * Class mockClass = enhancer.createClass();
		 * Enhancer.registerCallbacks(mockClass, new Callback[] {
		 * mockMethodInterceptor });
		 */

		// Factory com.allbuy.compare.core.persistence.mock = ObjenesisHelper.newInstance(mockClass);
		Factory mock = (Factory) enhancer.create();
		mock.getCallback(0);
		return (T) mock;
	}
}

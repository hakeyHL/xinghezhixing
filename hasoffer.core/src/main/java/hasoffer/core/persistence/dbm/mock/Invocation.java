package hasoffer.core.persistence.dbm.mock;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Invocation {
	private Object obj;
	private Method method;
	private Object[] args;
	private MethodProxy methodProxy;

	public Invocation(Object obj, Method method, Object[] args,
			MethodProxy methodProxy) {
		super();
		this.obj = obj;
		this.method = method;
		this.args = args;
		this.methodProxy = methodProxy;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public void setMethodProxy(MethodProxy methodProxy) {
		this.methodProxy = methodProxy;
	}

}

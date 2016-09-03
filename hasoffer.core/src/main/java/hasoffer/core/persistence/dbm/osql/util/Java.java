package hasoffer.core.persistence.dbm.osql.util;

public class Java {
	private static boolean rightFirst = false;
	
	private static boolean callLeft = false;
	
	static {
		test(left(), right());
	}

	public static boolean isRightFirst() {
		return rightFirst;
	}
	
	public static boolean isLeftFirst() {
		return !rightFirst;
	}

	private static Object left() {
		callLeft = true;
		return null;
	}

	private static Object right() {
		rightFirst = !callLeft;

		return null;
	}

	private static void test(Object left, Object right) {

	}

	public static void main(String[] args) {
		System.out.print(Java.isRightFirst());
	}
}

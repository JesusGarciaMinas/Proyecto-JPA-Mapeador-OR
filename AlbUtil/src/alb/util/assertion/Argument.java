package alb.util.assertion;

public abstract class Argument {

	public static void isNotNull(final Object obj) {
		isTrue( obj != null, " Cannot be null " );
	}

	public static void isNull(final Object obj) {
		isTrue( obj == null, " Must be null " );
	}

	public static void isTrue(final boolean test) {
		isTrue(test, "");
	}

	public static void isTrue(final boolean test, final String msg) {
		if (test == true) return;
		throwException(msg);
	}

	protected static void throwException(final String msg) {
		throw new IllegalArgumentException( msg );
	}

}

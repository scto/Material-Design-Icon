package org.view.common.util;

public class ThrowableUtils {

	public static void throwRuntimeException(String msg, Throwable e) {
		throwRuntimeException(new RuntimeException(msg, e));
	}

	public static void throwRuntimeException(Throwable e) {
		if (e instanceof RuntimeException) {
			throw (RuntimeException)e;
		} else {
			throw new RuntimeException(e);
		}
	}

}

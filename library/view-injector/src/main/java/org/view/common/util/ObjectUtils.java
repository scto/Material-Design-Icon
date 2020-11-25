package org.view.common.util;

public class ObjectUtils {

    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        if (obj == null) return defaultObj;
        return obj;
    }

    public static boolean requireNonNull(Object... objs) {
        if (objs == null) throw new NullPointerException();
        for (int i=0;i < objs.length;i++) {
            requireNonNull(objs[i]);
        }
		return true;
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean isExtendsClass(Class<?> extendsKind, Class<?> kind) {
        for (Class<?> superClass = extendsKind ;superClass != null; superClass = superClass.getSuperclass()) {
            if (superClass == kind) return true;
        }
        return false;
    }

}

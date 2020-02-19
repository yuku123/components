
package com.zifang.util.core.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeUtil {

	private static final Unsafe UNSAFE;
	private static final long STRING_VALUE_FIELD_OFFSET;
	private static final long STRING_OFFSET_FIELD_OFFSET;
	private static final long STRING_COUNT_FIELD_OFFSET;

	static {
		Unsafe unsafe = null;
		long stringValueFieldOffset = -1L;
		long stringOffsetFieldOffset = -1L;
		long stringCountFieldOffset = -1L;

		if (System.getProperty("java.version").startsWith("1.8")) {
			try {
				final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				unsafe = (Unsafe) unsafeField.get(null);
			} catch (Throwable cause) {
				unsafe = null;
			}

			if (unsafe != null) {
				try {
					stringValueFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
					stringOffsetFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("offset"));
					stringCountFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("count"));
				} catch (Throwable ignore) {
				}
			}
		}

		UNSAFE = unsafe;
		STRING_VALUE_FIELD_OFFSET = stringValueFieldOffset;
		STRING_OFFSET_FIELD_OFFSET = stringOffsetFieldOffset;
		STRING_COUNT_FIELD_OFFSET = stringCountFieldOffset;
	}


	/**
	 * 获得一个unsafe实例类
	 *
	 * unsafe实例是不能被jdk外的代码获取的
	 *
	 * */
	public static Unsafe getUnsageInstance(){
		Field f = null;
		Unsafe unsafe = null;
		try {
			f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			unsafe = (Unsafe) f.get(null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return unsafe;

	}

	static char[] unsafeGetChars(final String string) {
		final char[] value = (char[]) UNSAFE.getObject(string, STRING_VALUE_FIELD_OFFSET);

		if (STRING_OFFSET_FIELD_OFFSET != -1) {
			// old String version with offset and count
			final int offset = UNSAFE.getInt(string, STRING_OFFSET_FIELD_OFFSET);
			final int count = UNSAFE.getInt(string, STRING_COUNT_FIELD_OFFSET);

			if (offset == 0 && count == value.length) {
				// no need to copy
				return value;

			} else {
				final char[] result = new char[count];
				System.arraycopy(value, offset, result, 0, count);
				return result;
			}

		} else {
			return value;
		}
	}

	/**
	 * Returns <code>true</code> if system has the <code>Unsafe</code>.
	 */
	public static Unsafe getUnsafeInstance() {
		return UNSAFE;
	}

}
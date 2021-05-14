package com.zifang.util.core.lang;


import java.lang.reflect.Array;
import java.util.List;

/**
 * 数组相关的工具类
 */
public class ArraysUtil {

    /**
     * 转换多个元素为整个数组
     */
    public static <T> T[] array(T... elements) {
        return elements;
    }


    /**
     * 合并多个数组变为一个数组
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] join(T[]... arrays) {
        Class<T> componentType = (Class<T>) arrays.getClass().getComponentType().getComponentType();
        return join(componentType, arrays);
    }

    /**
     * Joins arrays using provided component type.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] join(Class<T> componentType, T[][] arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        int length = 0;
        for (T[] array : arrays) {
            length += array.length;
        }
        T[] result = (T[]) Array.newInstance(componentType, length);

        length = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, length, array.length);
            length += array.length;
        }
        return result;
    }
    // ---------------------------------------------------------------- resize

    /**
     * Resizes an array.
     */
    public static <T> T[] resize(T[] buffer, int newSize) {
        Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
        T[] temp = (T[]) Array.newInstance(componentType, newSize);
        System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
        return temp;
    }

    // ---------------------------------------------------------------- append

    /**
     * Appends an element to array.
     */
    public static <T> T[] append(T[] buffer, T newElement) {
        T[] t = resize(buffer, buffer.length + 1);
        t[buffer.length] = newElement;
        return t;
    }

    public static <T> T[] append(T[] buffer, T[] newElement) {
        List<T> list = java.util.Arrays.asList(buffer);
        list.addAll(java.util.Arrays.asList(newElement));
        return (T[]) list.toArray();
    }

    /**
     * Removes sub-array.
     */
    public static <T> T[] remove(T[] buffer, int offset, int length) {
        Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
        return remove(buffer, offset, length, componentType);
    }

    /**
     * Removes sub-array.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] remove(T[] buffer, int offset, int length, Class<T> componentType) {
        int len2 = buffer.length - length;
        T[] temp = (T[]) Array.newInstance(componentType, len2);
        System.arraycopy(buffer, 0, temp, 0, offset);
        System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
        return temp;
    }

    /**
     * Returns subarray.
     */
    public static <T> T[] subarray(T[] buffer, int offset, int length) {
        Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
        return subarray(buffer, offset, length, componentType);
    }

    /**
     * Returns subarray.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] subarray(T[] buffer, int offset, int length, Class<T> componentType) {
        T[] temp = (T[]) Array.newInstance(componentType, length);
        System.arraycopy(buffer, offset, temp, 0, length);
        return temp;
    }

    // ---------------------------------------------------------------- insert

    /**
     * Inserts one array into another array.
     */
    public static <T> T[] insert(T[] dest, T[] src, int offset) {
        Class<T> componentType = (Class<T>) dest.getClass().getComponentType();
        return insert(dest, src, offset, componentType);
    }

    /**
     * Inserts one element into an array.
     */
    public static <T> T[] insert(T[] dest, T src, int offset) {
        Class<T> componentType = (Class<T>) dest.getClass().getComponentType();
        return insert(dest, src, offset, componentType);
    }

    /**
     * Inserts one array into another array.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] insert(T[] dest, T[] src, int offset, Class componentType) {
        T[] temp = (T[]) Array.newInstance(componentType, dest.length + src.length);
        System.arraycopy(dest, 0, temp, 0, offset);
        System.arraycopy(src, 0, temp, offset, src.length);
        System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
        return temp;
    }

    /**
     * Inserts one element into another array.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] insert(T[] dest, T src, int offset, Class componentType) {
        T[] temp = (T[]) Array.newInstance(componentType, dest.length + 1);
        System.arraycopy(dest, 0, temp, 0, offset);
        temp[offset] = src;
        System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
        return temp;
    }

    /**
     * Inserts one array into another at given offset.
     */
    public static <T> T[] insertAt(T[] dest, T[] src, int offset) {
        Class<T> componentType = (Class<T>) dest.getClass().getComponentType();
        return insertAt(dest, src, offset, componentType);
    }

    /**
     * Inserts one array into another at given offset.
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] insertAt(T[] dest, T[] src, int offset, Class componentType) {
        T[] temp = (T[]) Array.newInstance(componentType, dest.length + src.length - 1);
        System.arraycopy(dest, 0, temp, 0, offset);
        System.arraycopy(src, 0, temp, offset, src.length);
        System.arraycopy(dest, offset + 1, temp, src.length + offset, dest.length - offset - 1);
        return temp;
    }


    /**
     * Converts to primitive array.
     */
    public static byte[] values(Byte[] array) {
        byte[] dest = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            Byte v = array[i];
            if (v != null) {
                dest[i] = v.byteValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Byte[] valuesOf(byte[] array) {
        Byte[] dest = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Byte.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static char[] values(Character[] array) {
        char[] dest = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            Character v = array[i];
            if (v != null) {
                dest[i] = v.charValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Character[] valuesOf(char[] array) {
        Character[] dest = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Character.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static short[] values(Short[] array) {
        short[] dest = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            Short v = array[i];
            if (v != null) {
                dest[i] = v.shortValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Short[] valuesOf(short[] array) {
        Short[] dest = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Short.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static int[] values(Integer[] array) {
        int[] dest = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            Integer v = array[i];
            if (v != null) {
                dest[i] = v.intValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Integer[] valuesOf(int[] array) {
        Integer[] dest = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Integer.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static long[] values(Long[] array) {
        long[] dest = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            Long v = array[i];
            if (v != null) {
                dest[i] = v.longValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Long[] valuesOf(long[] array) {
        Long[] dest = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Long.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static float[] values(Float[] array) {
        float[] dest = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            Float v = array[i];
            if (v != null) {
                dest[i] = v.floatValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Float[] valuesOf(float[] array) {
        Float[] dest = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Float.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static double[] values(Double[] array) {
        double[] dest = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            Double v = array[i];
            if (v != null) {
                dest[i] = v.doubleValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Double[] valuesOf(double[] array) {
        Double[] dest = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Double.valueOf(array[i]);
        }
        return dest;
    }


    /**
     * Converts to primitive array.
     */
    public static boolean[] values(Boolean[] array) {
        boolean[] dest = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            Boolean v = array[i];
            if (v != null) {
                dest[i] = v.booleanValue();
            }
        }
        return dest;
    }

    /**
     * Converts to object array.
     */
    public static Boolean[] valuesOf(boolean[] array) {
        Boolean[] dest = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            dest[i] = Boolean.valueOf(array[i]);
        }
        return dest;
    }


    // ---------------------------------------------------------------- indexof


    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(byte[] array, byte value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(byte[] array, byte value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(byte[] array, byte value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(byte[] array, byte value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(char[] array, char value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(char[] array, char value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(char[] array, char value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(char[] array, char value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(short[] array, short value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(short[] array, short value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(short[] array, short value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(short[] array, short value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(int[] array, int value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(int[] array, int value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(int[] array, int value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(long[] array, long value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(long[] array, long value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(long[] array, long value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(long[] array, long value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of an element in an array.
     */
    public static int indexOf(boolean[] array, boolean value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if an array contains given value.
     */
    public static boolean contains(boolean[] array, boolean value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in an array from specified given position.
     */
    public static int indexOf(boolean[] array, boolean value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(boolean[] array, boolean value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of value in <code>float</code> array.
     */
    public static int indexOf(float[] array, float value) {
        for (int i = 0; i < array.length; i++) {
            if (Float.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if <code>float</code> array contains given value.
     */
    public static boolean contains(float[] array, float value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in <code>float</code>
     * array from specified given position.
     */
    public static int indexOf(float[] array, float value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (Float.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in <code>float</code> array from specified given position and upto given length.
     */
    public static int indexOf(float[] array, float value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (Float.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence of value in <code>double</code> array.
     */
    public static int indexOf(double[] array, double value) {
        for (int i = 0; i < array.length; i++) {
            if (Double.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns <code>true</code> if <code>double</code> array contains given value.
     */
    public static boolean contains(double[] array, double value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence of given value in <code>double</code>
     * array from specified given position.
     */
    public static int indexOf(double[] array, double value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (Double.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in <code>double</code> array from specified given position and upto given length.
     */
    public static int indexOf(double[] array, double value, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if (Double.compare(array[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(Object[] array, Object value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(Object[] array, Object value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(Object[] array, Object value, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(Object[] array, Object value, int startIndex) {
        return indexOf(array, value, startIndex) != -1;
    }


    // ---------------------------------------------------------------- indexof 2


    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(byte[] array, byte[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(byte[] array, byte[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(byte[] array, byte[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(byte[] array, byte[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        byte c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(char[] array, char[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(char[] array, char[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(char[] array, char[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(char[] array, char[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        char c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(short[] array, short[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(short[] array, short[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(short[] array, short[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(short[] array, short[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        short c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(int[] array, int[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(int[] array, int[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(int[] array, int[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(int[] array, int[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        int c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(long[] array, long[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(long[] array, long[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(long[] array, long[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(long[] array, long[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        long c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(boolean[] array, boolean[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(boolean[] array, boolean[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(boolean[] array, boolean[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(boolean[] array, boolean[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        boolean c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (array[i] != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (sub[j] != array[k]) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(float[] array, float[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(float[] array, float[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(float[] array, float[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(float[] array, float[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        float c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (Float.compare(array[i], c) != 0) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (Float.compare(sub[j], array[k]) != 0) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    /**
     * Finds the first occurrence in an array.
     */
    public static int indexOf(double[] array, double[] sub) {
        return indexOf(array, sub, 0, array.length);
    }

    public static boolean contains(double[] array, double[] sub) {
        return indexOf(array, sub) != -1;
    }


    /**
     * Finds the first occurrence in an array from specified given position.
     */
    public static int indexOf(double[] array, double[] sub, int startIndex) {
        return indexOf(array, sub, startIndex, array.length);
    }

    /**
     * Finds the first occurrence in an array from specified given position and upto given length.
     */
    public static int indexOf(double[] array, double[] sub, int startIndex, int endIndex) {
        int sublen = sub.length;
        if (sublen == 0) {
            return startIndex;
        }
        int total = endIndex - sublen + 1;
        double c = sub[0];
        mainloop:
        for (int i = startIndex; i < total; i++) {
            if (Double.compare(array[i], c) != 0) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                if (Double.compare(sub[j], array[k]) != 0) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }


    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(String[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(byte[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(char[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(short[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(int[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(long[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(float[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(double[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * Converts an array to string array.
     */
    public static String[] toStringArray(boolean[] array) {
        if (array == null) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = String.valueOf(array[i]);
        }
        return result;
    }

    /**
     * 判断数组是否 为空
     *
     * @param array
     * @return boolean
     */
    private static <T> boolean isEmptyArray(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否 不为空
     *
     * @param array
     * @return boolean
     */
    public static <T> boolean isNotEmptyArray(T[] array) {
        return array != null && array.length > 0;
    }

    public static <T> boolean isDeeplyEqual(T[] array1, T[] array2) {
        if (array1 == null || array2 == null) { //清除参数是null的可能性
            return array1 == array2;
        } else if (array1.length != array2.length) { //排除length大小不同的问题
            return false;
        } else {
            for (int i = 0; i < array1.length; i++) {  //遍历判断是否完全一致
                if (!array1[i].equals(array2[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
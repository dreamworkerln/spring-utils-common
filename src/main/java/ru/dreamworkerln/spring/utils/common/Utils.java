package ru.dreamworkerln.spring.utils.common;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class Utils {

    public static void throwIfNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    public String boolToStr(boolean b) {
        return b ? "1" : "0";
    }

    public Set<String> rolesToSet(Object authorities) {
        //noinspection unchecked
        return new HashSet<>(((List<String>) authorities));
    }

    public long toLong(String s) {
        return Long.parseLong(s);
    }

    public int toInt(String s) {
        return Integer.parseInt(s);
    }



    /**
     * Get first field of specified class
     * @param source searching in this class
     * @param pattern which class to find
     * @return field name
     */
    public String getPatternClassFieldName(Class<?> source, Class<?> pattern) {

        String result = null;

        outerLoop:
        do {
            for (Field f : source.getDeclaredFields()) {
                if (f.getType() == pattern) {
                    result = f.getName();
                    break outerLoop;
                }
            }
        }
        while((source = source.getSuperclass()) != null);

        return result;
    }


    /**
     * Set object field value
     * @param fieldName
     * @param o object
     * @param value new field value
     */
    @SneakyThrows
    public void fieldSetter(String fieldName, final Object o, final Object value) {

        Class<?> clazz = o.getClass();
        Field field = null;
        do {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch(Exception ignore) {}
        }
        while((clazz = clazz.getSuperclass()) != null);  // Пролезет через CGLIB proxy

        Assert.notNull(field, "field == null");
        field.setAccessible(true);
        field.set(o, value);

    }


    /**
     * Invoke object setter
     * @param setterName setter name
     * @param o object
     * @param paramType setter param type
     * @param value new value
     */
    @SneakyThrows
    public void propertySetter(String setterName, final Object o, Class<?> paramType, final Object value) {


        Class<?> clazz = o.getClass();
        Method method = null;
        do {
            try {
                method = clazz.getDeclaredMethod(setterName, paramType);
            } catch(Exception ignore) {}
        }
        while((clazz = clazz.getSuperclass()) != null);

        Assert.notNull(method, "method == null");
        method.setAccessible(true);
        method.invoke(o, value);
    }


    /**
     * Read object field value
     * @param fieldName field name
     * @param o object
     * @return
     */
    @SneakyThrows
    public Object fieldGetter(String fieldName, final Object o) {

        Class<?> clazz = o.getClass();
        Field field = null;
        do {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch(Exception ignore) {}
        }
        while((clazz = clazz.getSuperclass()) != null);

        Assert.notNull(field, "field == null");
        field.setAccessible(true);
        return field.get(o);
    }


}


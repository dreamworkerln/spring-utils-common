package ru.dreamworkerln.spring.utils.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

// https://stackoverflow.com/a/19739041/13174445
public class SpringBeanUtilsEx {

    public static String[] getNullPropertyNames (Object source) {

        // Add null fields from source
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    /**
     * Copy upper level non-null fields from source to target
     * @param source from
     * @param target to
     */
    public static void copyPropertiesExcludeNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    @SneakyThrows
    public static Object cloneBean(Object source) {
        return org.apache.commons.beanutils.BeanUtils.cloneBean(source);
    }
}

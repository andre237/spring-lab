package com.training.spring.lab.utils.tools;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class ApplicationUtils {

    /**
     * One-liner to copy class attributes which match on name and type
     *
     * @param source the object from which the properties will be copied
     * @param target the object for which to copy the properties
     * @param <T> generic type
     * @return the same instance of @param target, but with the copied props
     */
    public static <T> T copyProperties(Object source, T target, String... ignore) {
        BeanUtils.copyProperties(source, target, ignore);
        return target;
    }

    public static <T> T copyNonNullProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    /**
     * Utility function to get the name of all members of an object with null value
     * to be used as an ignore criteria to {@link org.springframework.beans.BeanUtils#copyProperties(Object, Object, String...)}
     *
     * @param source the object to be tested
     * @return an array of string with the names of all null-valued members
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

}

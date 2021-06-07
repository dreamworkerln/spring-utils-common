package ru.dreamworkerln.spring.utils.common.configurations.annotations;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface AutowireClassList {
    Class<?>[] value() default {};
}

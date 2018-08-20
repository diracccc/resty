package io.dirac.rest.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceUri {
    String value();

    String method() default "POST";
}

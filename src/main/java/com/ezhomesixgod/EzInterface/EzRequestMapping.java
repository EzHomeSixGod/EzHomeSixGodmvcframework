package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface EzRequestMapping {

    String value() default "";
}

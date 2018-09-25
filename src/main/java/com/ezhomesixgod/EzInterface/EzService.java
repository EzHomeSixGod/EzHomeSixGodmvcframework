package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EzService {

    String value() default "";
}

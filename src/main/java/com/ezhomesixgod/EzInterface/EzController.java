package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EzController {
    String value() default "";
}

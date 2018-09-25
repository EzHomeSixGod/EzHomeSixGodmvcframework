package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface EzRequestParam {

    String value() default "";
}

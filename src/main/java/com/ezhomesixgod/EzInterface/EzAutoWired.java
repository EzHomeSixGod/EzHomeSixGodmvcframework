package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EzAutoWired {

    String value() default "";
}

package com.ezhomesixgod.EzInterface;

import java.lang.annotation.*;

public @interface EzResponseBody {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD,ElementType.TYPE})
    public @interface EzRequestParam {

        String value() default "";
    }
}

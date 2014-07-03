package com.division.freeforall.engines;

import java.lang.annotation.*;

/**
 *
 * @author Evan
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EngineInfo {

    String author() default "";

    String version() default "0.0.1d";

    String[] depends() default {};
}

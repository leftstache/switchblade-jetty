package com.leftstache.switchblade.jetty;

import java.lang.annotation.*;

/**
 * @author Joel Johnson
 */
@Target(value = { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Endpoint {
	String path() default "";
}

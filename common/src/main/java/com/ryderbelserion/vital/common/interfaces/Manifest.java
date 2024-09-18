package com.ryderbelserion.vital.common.interfaces;

import com.ryderbelserion.vital.common.enums.Environment;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {

    Environment environment();

}
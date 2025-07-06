package com.example.demo.aspect.annotation;

import com.example.demo.aspect.enumeration.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {
    LogLevel level() default LogLevel.INFO;
 
}
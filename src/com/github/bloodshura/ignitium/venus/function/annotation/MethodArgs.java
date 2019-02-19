package com.github.bloodshura.ignitium.venus.function.annotation;

import com.github.bloodshura.ignitium.venus.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MethodArgs {
	Class<? extends Value>[] value();
}
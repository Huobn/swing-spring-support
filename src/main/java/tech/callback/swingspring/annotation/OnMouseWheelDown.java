package tech.callback.swingspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EventBase
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMouseWheelDown
{
    String methodName() default "";
}

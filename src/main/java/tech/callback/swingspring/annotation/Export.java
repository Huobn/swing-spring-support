package tech.callback.swingspring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将某个视图类中的组件暴露给Spring IOC容器。 若某个字段标注了此注解，
 * 则你可以通过Spring IOC容器索引到这个字段的实例（Nullable）。
 * @author callback
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Export
{
    String name() default "";
}

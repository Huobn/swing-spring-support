package tech.callback.swingspring.annoataion;

import org.springframework.context.annotation.DependsOn;
import tech.callback.swingspring.support.SwingApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定控制器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindController
{
    Class<?> controller();
}

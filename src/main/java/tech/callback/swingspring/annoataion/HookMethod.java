package tech.callback.swingspring.annoataion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 钩子函数注解，被此注解标注的方法，会在SpringBean初始化完毕后，自动调用。
 * @author callback
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HookMethod
{
}

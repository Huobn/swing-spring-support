package tech.callback.swingspring.annotation;

import java.lang.annotation.*;

/**
 * 钩子函数注解，被此注解标注的方法，会在SpringBean初始化完毕后，自动调用。
 *  可以通过设定注解的order值来确定钩子函数的执行顺序，优先级越高，order越小，默认值为最高优先级
 * @author callback
 * @version 1.0.1
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HookMethod
{
    int order() default -1;
}

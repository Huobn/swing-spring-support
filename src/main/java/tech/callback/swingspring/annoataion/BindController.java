package tech.callback.swingspring.annoataion;

import org.springframework.context.annotation.DependsOn;
import tech.callback.swingspring.support.SwingApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制绑定注解，若一个类标注了此类型的注解，则其组件的事件处理函数会自动绑定为控制器中的函数。
 * @author callback
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindController
{
    Class<?> controller();
}

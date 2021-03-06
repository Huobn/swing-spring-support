package tech.callback.swingspring.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为某个字段添加鼠标退出事件。注意，被标注的字段必须为JComponent类型，否则则注解将不生效。
 * @author callback
 * @version 1.0.0
 */
@EventBase
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMouseExit
{
    String methodName() default "";
}

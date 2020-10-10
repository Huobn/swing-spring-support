package tech.callback.swingspring.annotation;

import tech.callback.swingspring.support.MouseButton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为某个字段标添加鼠标点击事件，注意，被标注的字段必须为JComponent类型，否则则注解将不生效。
 * @author callback
 * @version 1.0.0
 */
@EventBase
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick
{
    String methodName() default "";

    int clickCount() default 1;

    MouseButton clickButton() default MouseButton.LEFT;
}

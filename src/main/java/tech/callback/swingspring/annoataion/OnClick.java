package tech.callback.swingspring.annoataion;

import tech.callback.swingspring.support.ClickButton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick
{
    String methodName() default "";

    int clickCount() default 1;

    ClickButton clickButton() default ClickButton.LEFT;
}

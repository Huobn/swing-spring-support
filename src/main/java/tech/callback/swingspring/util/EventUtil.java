package tech.callback.swingspring.util;

import tech.callback.swingspring.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Hashtable;
import java.util.function.Function;

/**
 * 事件处理工具类
 */
public class EventUtil
{
    private static final Hashtable<Class<? extends Annotation>, Function<String, String>> methodNameGenerateFactory
            = new Hashtable<>();

    static
    {
        methodNameGenerateFactory.put(
                OnClick.class, EventUtil::onClickMethodName
        );

        methodNameGenerateFactory.put(
                OnMouseEnter.class, EventUtil::onMouseEnterMethodName
        );

        methodNameGenerateFactory.put(
                OnMouseExit.class, EventUtil::onMouseExitMethodName
        );

        methodNameGenerateFactory.put(
                OnMouseRelease.class, EventUtil::onMouseReleaseMethodName
        );

        methodNameGenerateFactory.put(
                OnMousePress.class, EventUtil::onMousePressMethodName
        );

        methodNameGenerateFactory.put(
                OnMouseWheelDown.class, EventUtil::onMouseWheelDownMethodName
        );

        methodNameGenerateFactory.put(
                OnMouseWheelUp.class, EventUtil::onMouseWheelUpMethodName
        );
    }

    private static String onClickMethodName(String salt)
    {
        return "on" + upperCaseFirstLetter(salt) + "Click";
    }

    private static String onMouseEnterMethodName(String salt)
    {
        return "on" + upperCaseFirstLetter(salt) + "MouseEnter";
    }

    private static String onMouseExitMethodName(String fieldName)
    {
        return "on" + upperCaseFirstLetter(fieldName) + "MouseExit";
    }

    private static String onMouseReleaseMethodName(String fieldName)
    {
        return "on" + upperCaseFirstLetter(fieldName) + "MouseRelease";
    }

    private static String onMousePressMethodName(String fieldName)
    {
        return "on" + upperCaseFirstLetter(fieldName) + "MousePress";
    }

    private static String onMouseWheelUpMethodName(String fieldName)
    {
        return "on" + upperCaseFirstLetter(fieldName) + "MouseWheelUp";
    }

    private static String onMouseWheelDownMethodName(String fieldName)
    {
        return "on" + upperCaseFirstLetter(fieldName) + "MouseWheelDown";
    }


    private static String upperCaseFirstLetter(String str)
    {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }


    public static String getDefaultMethodName(Class<? extends Annotation> annotationClass, String fieldName)
    {
        Function<String, String> function = methodNameGenerateFactory.get(annotationClass);
        if (function != null) {
            return function.apply(fieldName);
        } else {
            return "";
        }
    }
}

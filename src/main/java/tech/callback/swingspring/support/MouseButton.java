package tech.callback.swingspring.support;

/**
 * 鼠标按键枚举类
 */
public enum MouseButton
{
    LEFT(1), MIDDLE(2), RIGHT(3);

    public final int code;
    MouseButton(int code) {this.code = code;}
}

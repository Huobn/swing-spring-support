package tech.callback.swingspring.support;

/**
 * 鼠标行为枚举类
 */
public enum MouseAction
{
    ENTER(0b1), EXIT(0b10), PRESS(0b100), RELEASE(0b1000),CLICK(0b10000),
    WHEEL_UP(-1), WHEEL_DOWN(1);

    public final int code;

    MouseAction(int code) {this.code = code;}
}

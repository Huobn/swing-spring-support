package tech.callback.swingspring.handler;

import org.springframework.context.ApplicationContext;
import tech.callback.swingspring.support.MouseButton;
import tech.callback.swingspring.support.DecoratedEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 鼠标点击事件处理器, 用反射的方式处理鼠标点击事件。
 * 在代码中不需要用户显示的为某个组件添加点击事件监听器，转而使用注解标注的方式，来添加监听器和事件处理函数。
 * @author callback
 * @version 1.0.0
 */
public class MouseClickEventHandler implements MouseListener
{
    private final Object targetRef; /* 执行target */
    private final Method handlerRef; /*· 处理器 */
    private final int clickCount; /* 点击次数 */
    private final MouseButton clickButton; /* 点击按钮 */
    private final ApplicationContext contextRef; /* 容器 */

    public MouseClickEventHandler(Object target, Method handler, int clickCount, MouseButton clickButton,
                                  ApplicationContext context)
    {
        this.targetRef = target;
        this.handlerRef = handler;
        this.clickCount = clickCount;
        this.clickButton = clickButton;
        this.contextRef = context;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == clickCount
                && e.getButton() == clickButton.code) {
            try {
                handlerRef.invoke(targetRef, new DecoratedEvent(e, contextRef));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace(); /* 打印信息 */
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}

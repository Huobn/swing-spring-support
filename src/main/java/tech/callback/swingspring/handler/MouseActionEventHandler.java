package tech.callback.swingspring.handler;

import org.springframework.context.ApplicationContext;
import tech.callback.swingspring.support.DecoratedEvent;
import tech.callback.swingspring.support.MouseAction;
import tech.callback.swingspring.support.MouseButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 鼠标行为处理器，支持处理的鼠标行为有：
 *  鼠标进入，鼠标移出，鼠标按键按下，鼠标按键松开，分别对应鼠标行为枚举类 ENTER, EXIT, PRESS, RELEASE
 * @author callback
 * @version 1.0.0
 */
public class MouseActionEventHandler implements MouseListener
{
    private final Object targetRef;
    private final Method handlerRef;
    private final ApplicationContext contextRef;
    private final MouseButton button;
    private final MouseAction action;

    public MouseActionEventHandler(Object targetRef, Method handlerRef,
                                   MouseButton button, MouseAction action, ApplicationContext contextRef)
    {
        this.targetRef = targetRef;
        this.handlerRef = handlerRef;
        this.contextRef = contextRef;
        this.button = button;
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (MouseAction.PRESS.equals(action)
                && e.getButton() == button.code) {
            try {
                handlerRef.invoke(targetRef, new DecoratedEvent(e, contextRef));
            }catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (MouseAction.RELEASE.equals(action)
                && e.getButton() == button.code) {

            try {
                handlerRef.invoke(targetRef, new DecoratedEvent(e, contextRef));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (MouseAction.ENTER.equals(action)) {
            try {
                handlerRef.invoke(targetRef, new DecoratedEvent(e, contextRef));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (MouseAction.EXIT.equals(action)) {
            try {
                handlerRef.invoke(targetRef, new DecoratedEvent(e, contextRef));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package tech.callback.swingspring.handler;

import org.springframework.context.ApplicationContext;
import tech.callback.swingspring.support.DecoratedEvent;
import tech.callback.swingspring.support.MouseAction;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MouseWheelActionHandler implements MouseWheelListener
{
    private final ApplicationContext context;
    private final Method methodRef;
    private final Object targetRef;
    private final MouseAction action;

    public MouseWheelActionHandler(ApplicationContext context, Method methodRef, Object targetRef, MouseAction action)
    {
        this.context = context;
        this.methodRef = methodRef;
        this.targetRef = targetRef;
        this.action = action;

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (action.code == e.getWheelRotation()) {
            try {
                methodRef.invoke(targetRef, new DecoratedEvent(e, context));
            } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
    }
}

package tech.callback.swingspring.support;

import org.springframework.context.ApplicationContext;

import java.awt.*;

/**
 * 装饰之后的Event,凡是通过Swing-Spring-Support注解进行绑定的事件处理的函数，其形式参数必须为此类型。
 * 装饰之后的Event，能够引用到容器上下文，能够细粒度的获取到容器中的组件
 * @author callback
 * @version 1.0.0
 */
public  class DecoratedEvent
{
    private final AWTEvent eventRef;
    private final ApplicationContext contextRef;

    public DecoratedEvent(AWTEvent event, ApplicationContext context)
    {
        eventRef = event;
        contextRef = context;
    }

    @SuppressWarnings("unchecked")
    public <T extends AWTEvent> T getEvent(Class<T> event)
    {
        /*unchecked*/
        return (T)eventRef;
    }

    public Object getComponent(String qualifierName) { return contextRef.getBean(qualifierName); }

    public <E> E getComponent(String qualifierName, Class<E> clazz) { return contextRef.getBean(qualifierName, clazz); }

    public <E> E getComponent(Class<E> clazz) { return contextRef.getBean(clazz); }
}

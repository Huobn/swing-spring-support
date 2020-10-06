package tech.callback.swingspring.support;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.callback.swingspring.annoataion.*;
import tech.callback.swingspring.handler.MouseActionEventHandler;
import tech.callback.swingspring.handler.MouseClickEventHandler;

import javax.swing.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * CommonProcessor通用处理器，核心类。此类将作为组件注入到Spring IOC容器中，通过此组件监听Bean的创建过程，并扩展Bean的属性。
 *  功能支持：
 *      - 导出组件到IOC容器
 *      - 为某个视图组件添加事件监听器
 *      - 为视图绑定控制器
 *      - 执行钩子函数
 * @author callback
 * @version 1.0.0
 */
@Slf4j
public class CommonProcessor implements BeanPostProcessor, ApplicationContextAware
{

    private ApplicationContext context;

    private final Hashtable<Object, Object> viewAndController = new Hashtable<>(); /* 视图和控制器 */

    /**
     * 处理绑定Controller
     * @param bean SpringBean
     */
    private void handleBindController(Object bean)
    {
        Optional.ofNullable(bean.getClass().getAnnotation(BindController.class))
                .ifPresent(controller-> viewAndController.put(bean, controller.controller()));
    }


    /**
     * 处理鼠标点击事件
     * @param bean SpringBean
     */
    private void handleMouseClickEvent(Object bean) throws IllegalAccessException
    {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        if (declaredFields.length == 0) return;

        /* 遍历bean的所有字段 */
        for (Field field : declaredFields) {
            field.setAccessible(true);
            OnClick onClick = field.getDeclaredAnnotation(OnClick.class);
            if (onClick == null) continue;

            if (field.get(bean) instanceof JComponent) {

                /* methodName若不填写，默认为 on<FieldName>Click */
                String methodName = "".equals(onClick.methodName()) ?
                        "on" + upperCaseFirstLetter(field.getName()) + "Click" : onClick.methodName();

                /* 检查这个Bean是否绑定了Controller，若绑定了Controller，则点击事件的默认处理方法为Controller中的方法 */
                Object target = Optional.ofNullable(viewAndController.get(bean))
                        .orElseGet(()->bean);

                /* 绑定的方法的访问级别必须为公开，否则无法访问, 并且必须有MouseEvent形式参数 */
                try {
                    Method method = target.getClass().getMethod(methodName,DecoratedEvent.class);
                    /* 添加鼠标点击事件处理器 */
                    ((JComponent)field.get(bean)).addMouseListener(
                            new MouseClickEventHandler(
                                    target, method, onClick.clickCount(), onClick.clickButton(), context
                            )
                    );
                } catch (NoSuchMethodException e) {
                    log.warn("Field<{}> click event bind unsuccessfully.", field.getName());
                }
            } else {
                log.warn("Filed<{}> unsupported event bind.", field.getName());
            }
        }

    }

    /**
     * 处理@Export注解的函数(Export 函数只可以导出JComponent的组件)
     * @param bean SpringBean
     */
    private void handleExport(Object bean) throws IllegalAccessException
    {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        if (declaredFields.length == 0) return;

        for (Field field : declaredFields) {
            field.setAccessible(true);
            Export export = field.getAnnotation(Export.class);
            if (export == null) continue;

            String beanName = "".equals(export.name()) ?
                    getReducedPackageName(bean.getClass().getPackage().getName()) + "."
                            + bean.getClass().getSimpleName() + "." + field.getName() : export.name();

            final AnnotationConfigApplicationContext ctx = (AnnotationConfigApplicationContext)context;

            if (field.get(bean) instanceof JComponent) {

                ctx.registerBean(beanName, JComponent.class, new Supplier<JComponent>()
                {
                    @SneakyThrows
                    @Override
                    public JComponent get()
                    {
                        return (JComponent) field.get(bean);
                    }
                });
                log.info("Add {} into spring container.", beanName);
            } else {
                log.warn("Field<{}> unsupported file type", field.getName());
            }
        }
    }


    /**
     * 处理@OnMouse*注解
     * @param bean SpringBean
     */
    private void handleOnMouse(Object bean) throws IllegalAccessException
    {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        if (declaredFields.length == 0) return;

        /* 遍历bean的所有字段 */
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (!(field.get(bean) instanceof JComponent)) continue;

            Annotation[] annotations = field.getAnnotations();

            if (annotations.length == 0) continue;

            for (Annotation annotation : annotations) {

                MouseButton button = MouseButton.LEFT;
                MouseAction action = MouseAction.ENTER;
                String methodName = null;

                if (annotation instanceof OnMouseEnter) {
                    final OnMouseEnter enter = (OnMouseEnter) annotation;
                    methodName = "".equals(enter.methodName()) ?
                            "on" + upperCaseFirstLetter(field.getName()) + "Mouse" +
                                    upperCaseFirstLetter(action.toString().toLowerCase())
                            : enter.methodName();
                } else if (annotation instanceof OnMouseExit) {
                    final OnMouseExit exit = (OnMouseExit) annotation;
                    action = MouseAction.EXIT;
                    methodName = "".equals(exit.methodName()) ?
                            "on" + upperCaseFirstLetter(field.getName()) + "Mouse" +
                                    upperCaseFirstLetter(action.toString().toLowerCase())
                            : exit.methodName();
                } else if (annotation instanceof OnMousePress) {
                    final OnMousePress press = (OnMousePress) annotation;
                    action = MouseAction.PRESS; button = press.button();
                    methodName = "".equals(press.methodName()) ?
                            "on" + upperCaseFirstLetter(field.getName()) + "Mouse" +
                                    upperCaseFirstLetter(action.toString().toLowerCase())
                            : press.methodName();
                } else if (annotation instanceof OnMouseRelease) {
                    final OnMouseRelease release = (OnMouseRelease) annotation;
                    action = MouseAction.RELEASE; button = release.button();
                    methodName = "".equals(release.methodName()) ?
                            "on" + upperCaseFirstLetter(field.getName()) + "Mouse" +
                                    upperCaseFirstLetter(action.toString().toLowerCase())
                            : release.methodName();
                }

                /* 检查这个Bean是否绑定了Controller，若绑定了Controller，则点击事件的默认处理方法为Controller中的方法 */
                Object target = Optional.ofNullable(viewAndController.get(bean))
                        .orElseGet(()->bean);

                if (methodName != null) {
                    /* 绑定的方法的访问级别必须为公开，否则无法访问, 并且必须有MouseEvent形式参数 */
                    try {
                        Method method = target.getClass().getMethod(methodName,DecoratedEvent.class);
                        /* 添加鼠标事件处理器 */
                        ((JComponent)field.get(bean)).addMouseListener(
                                new MouseActionEventHandler(
                                        target, method, button, action, context
                                )
                        );
                    } catch (NoSuchMethodException e) {
                        log.warn("Field<{}> mouse event bind unsuccessfully.", field.getName());
                    }
                }

            } // end-for


        }
    }


    /**
     * 处理钩子函数
     * @param bean SpringBean
     */
    private void handleHookMethod(Object bean)
    {
        Method[] methods = bean.getClass().getMethods();
        if (methods.length == 0) return;

        for (Method method : methods) {
            HookMethod annotation = method.getAnnotation(HookMethod.class);
            if (annotation == null) continue;
            try {
                method.invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        handleExport(bean);
        handleBindController(bean);
        handleMouseClickEvent(bean);
        handleOnMouse(bean);

        handleHookMethod(bean); /* 钩子函数处理器，一定要最后执行 */
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        context = applicationContext;
    }


    /*-------------------------- Util methods ----------------------*/

    private static String upperCaseFirstLetter(String str)
    {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private static String getReducedPackageName(String packageName)
    {
        StringBuilder builder = new StringBuilder();
        String[] split = packageName.split("\\.");
        if (split.length > 0) {
            for (String s : split) {
                builder.append(s.charAt(0));
                builder.append('.');
            }
        }
        return builder.substring(0, builder.length()-1);
    }
}

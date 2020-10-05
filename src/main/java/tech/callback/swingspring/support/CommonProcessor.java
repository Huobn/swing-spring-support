package tech.callback.swingspring.support;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.callback.swingspring.annoataion.BindController;
import tech.callback.swingspring.annoataion.Export;
import tech.callback.swingspring.annoataion.HookMethod;
import tech.callback.swingspring.annoataion.OnClick;
import tech.callback.swingspring.handler.MouseClickEventHandler;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * CommonProcessor用于处理Controller绑定，事件绑定
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

package tech.callback.swingspring.support;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Swing-Spring-Application，这个类为入口类，Application入口类需要继承此类，并在main方法中调用launch进行启动。
 * @author callback
 * @version 1.0.0
 */
@Slf4j(topic = "Application")
public class SwingApplication
{
    /* 线程池参数 */
    private static final int defaultCoreSize = 5;
    private static final int defaultMaximumSize = 5; /* fixed size pool */
    private static final long keepAliveTime = 300L;
    private static final int defaultMaximumWaiting = 10000; /* 线程队列中最大等待线程数 */


    /* Spring容器上下文对象 */
    private static final AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext();


    private static final LinkedList<Runnable> beforeLaunchTaskQueue = new LinkedList<>();
    private static final BlockingQueue<Runnable> backgroundTaskQueue =
            new LinkedBlockingQueue<>(defaultMaximumWaiting);

    private static final ThreadPoolExecutor backgroundTaskExecutor = new ThreadPoolExecutor(
            defaultCoreSize, defaultMaximumSize, keepAliveTime, TimeUnit.MILLISECONDS, backgroundTaskQueue,
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy()
    );

    public static void addBeforeLaunchTask(Runnable task)
    {
        beforeLaunchTaskQueue.add(task);
    }

    public static void addBeforeLaunchTaskFirst(Runnable task)
    {
        beforeLaunchTaskQueue.add(0, task);
    }

    public static void submitBackgroundTask(Runnable task)
    {
        backgroundTaskExecutor.execute(task);
    }

    public static int waitingBackgroundTask() { return backgroundTaskQueue.size(); }

    /**
     * Swing application启动入口函数
     * @param appClazz 程序入口类
     * @param frameClazz 主界面
     */
    public static void launch(Class<? extends SwingApplication> appClazz, Class<? extends AbstractFrame> frameClazz)
    {
        log.info("Thanks for using swing-spring-support.");

        if (!beforeLaunchTaskQueue.isEmpty()) {
            for (Runnable task : beforeLaunchTaskQueue) {
                try {
                    task.run();
                }catch (Exception ex) {
                    log.info("A task aborted for {}", ex.getMessage());
                }
            }
            beforeLaunchTaskQueue.clear();
        }

        if (appClazz != null && frameClazz != null) {
            final Package pkg = appClazz.getPackage();
            if (pkg == null) {
                log.info("No packages specified");System.exit(-1);
            }
            /* 注册通用处理器Bean */
            context.registerBean("commonProcessor", CommonProcessor.class, def->def.setScope("singleton"));
            context.scan(pkg.getName());
            context.registerBean("mainFrame", frameClazz, def->def.setScope("singleton"));
            context.refresh();
            /* 显示界面 */
            context.getBean(frameClazz).setVisible(true);
        } else {
            log.warn("Illegal launch arguments"); System.exit(-1);
        }

    }


    /* 获取容器上下文对象 */
    public static ApplicationContext getContext() { return context; }

}

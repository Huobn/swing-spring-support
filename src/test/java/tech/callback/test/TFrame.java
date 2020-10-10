package tech.callback.test;

import org.springframework.stereotype.Component;
import tech.callback.swingspring.annotation.*;
import tech.callback.swingspring.support.AbstractBorderFrame;
import tech.callback.swingspring.support.DecoratedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.TimeUnit;

@Component
@BindController(controller = TFrameController.class)
public class TFrame extends AbstractBorderFrame
{
    @OnClick
    private final JButton button = new JButton("click");

    @OnMouseWheelUp
    @OnMouseWheelDown
    private final JTextField textField = new JTextField();

    @Export(name = "tech.callback.jTextField")
    private final JTextField jTextField = new JTextField();


    public TFrame()
    {
        super();
        jTextField.setPreferredSize(new Dimension(200, 30));
        textField.setPreferredSize(new Dimension(200, 30));
    }


    @Override
    protected void customView()
    {
        super.customView();
        System.out.println("Hello world");
    }

    @Override
    protected JComponent createBottomPane()
    {
        System.out.println("create Bottom Pane");
        return button;
    }

    @Override
    protected JComponent createCentralPane()
    {
        return textField;
    }


    public void onTextFieldMouseWheelUp(DecoratedEvent e)
    {
        MouseWheelEvent event = e.getEvent(MouseWheelEvent.class);

        System.out.println("Mouse wheel up");
    }

    public void onTextFieldMouseWheelDown(DecoratedEvent e)
    {
        System.out.println("Mouse wheel down");
    }

    public void onButtonClick(DecoratedEvent e)
    {
        System.out.println("Hello world");
        TestApp.submitBackgroundTask(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(2000L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }

    public void onTextFieldMouseEnter(DecoratedEvent e)
    {
        System.out.println("Hello world");
    }

    public void onTextFieldMouseExit(DecoratedEvent e)
    {
        System.out.println("exit");
    }

    public void onTextFieldMousePress(DecoratedEvent e)
    {
        System.out.println("press");
    }

    public void onTextFieldMouseRelease(DecoratedEvent e)
    {
        System.out.println("release");
    }


    @HookMethod
    public void hook2()
    {
        final int a = 1 / 0;
        System.out.println("Hook2");
    }

    @HookMethod
    public void hook1()
    {
        System.out.println("Hook1");
    }
}

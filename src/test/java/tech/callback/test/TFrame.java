package tech.callback.test;

import tech.callback.swingc.toast.Toast;
import tech.callback.swingspring.annoataion.Export;
import tech.callback.swingspring.annoataion.HookMethod;
import tech.callback.swingspring.annoataion.OnClick;
import tech.callback.swingspring.support.AbstractFrame;
import tech.callback.swingspring.support.DecoratedEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class TFrame extends AbstractFrame
{
    @OnClick
    private JButton button = new JButton("click");

    @Export()
    private JTextField textField = new JTextField();

    @Export(name = "tech.callback.jTextField")
    private JTextField jTextField = new JTextField();


    public TFrame()
    {
        super();
        jTextField.setPreferredSize(new Dimension(200, 30));
        textField.setPreferredSize(new Dimension(200, 30));
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(button);
        getContentPane().add(textField);
        getContentPane().add(jTextField);
    }

    @HookMethod public void init()
    {
        System.out.println("hook");
    }

    public void onButtonClick(DecoratedEvent<MouseEvent> e)
    {
        ((JTextField)e.getComponent("t.c.t.TFrame.textField")).setText("Hello world");
        e.getComponent("tech.callback.jTextField", JTextField.class).setText("Hello");
        System.out.println("Hello world");
        TestApp.submitBackgroundTask(()->{
            try {
                TimeUnit.MILLISECONDS.sleep(2000L);
                Toast.showToast(this, Toast.CENTER, "background task completed!");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }
}

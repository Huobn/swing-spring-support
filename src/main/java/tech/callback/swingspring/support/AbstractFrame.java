package tech.callback.swingspring.support;

import tech.callback.swingspring.annotation.HookMethod;

import javax.swing.*;

/**
 * 抽象的JFrame类
 */
public abstract class AbstractFrame extends JFrame
{
    private static final int PREFERRED_WIDTH = 1000;
    private static final int PREFERRED_HEIGHT = 800;

    public AbstractFrame()
    {
        setVisible(false);
        setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @HookMethod protected void customView(){}
}

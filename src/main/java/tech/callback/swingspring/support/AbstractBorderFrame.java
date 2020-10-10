package tech.callback.swingspring.support;

import tech.callback.swingspring.annotation.HookMethod;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public abstract class AbstractBorderFrame extends AbstractFrame
{
    private final JPanel mainPane = new JPanel();
    public AbstractBorderFrame()
    {
        super();
        mainPane.setLayout(new BorderLayout());
        setContentPane(mainPane);
    }

    protected  JComponent createCentralPane()
    {
        return null;
    }

    protected JComponent createTopPane()
    {
        return null;
    }

    protected JComponent createLeftPane()
    {
        return null;
    }

    protected JComponent createRightPane()
    {
        return null;
    }

    protected JComponent createBottomPane()
    {
        return null;
    }

    @HookMethod public void setupPane()
    {
        Optional.ofNullable(createTopPane())
                .ifPresent(pane -> add(pane, BorderLayout.NORTH));

        Optional.ofNullable(createCentralPane())
                .ifPresent(pane -> add(pane, BorderLayout.CENTER));

        Optional.ofNullable(createLeftPane())
                .ifPresent(pane -> add(pane, BorderLayout.EAST));

        Optional.ofNullable(createRightPane())
                .ifPresent(pane -> add(pane, BorderLayout.WEST));

        Optional.ofNullable(createBottomPane())
                .ifPresent(pane -> mainPane.add(pane, BorderLayout.SOUTH));
    }
}

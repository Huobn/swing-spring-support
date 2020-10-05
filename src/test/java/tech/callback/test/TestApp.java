package tech.callback.test;

import com.pagosoft.plaf.PlafOptions;
import tech.callback.swingspring.support.SwingApplication;

import javax.swing.*;
import java.awt.*;


public class TestApp extends SwingApplication
{

    public static void main(String[] args)
    {
        //addBeforeLaunchTaskFirst(WebLookAndFeel::install);
        addBeforeLaunchTaskFirst(()->{
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
        });
        launch(TestApp.class, TFrame.class);
    }
}

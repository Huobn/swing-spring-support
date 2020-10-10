package tech.callback.test;

import org.springframework.stereotype.Controller;
import tech.callback.swingspring.support.DecoratedEvent;

@Controller
public class TFrameController
{
    public void onButtonClick(DecoratedEvent e)
    {
        System.out.println("Hello world");
    }
}

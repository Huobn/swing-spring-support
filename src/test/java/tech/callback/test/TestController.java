package tech.callback.test;

import org.springframework.stereotype.Controller;

@Controller
public class TestController
{
    public TestController()
    {
        System.out.println(getClass().getPackage().getName());
    }
}

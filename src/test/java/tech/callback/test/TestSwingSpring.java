package tech.callback.test;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import tech.callback.swingspring.annoataion.BindController;

@Component("testBean")
@BindController(controller = TestController.class)
public class TestSwingSpring
{

}

package tech.callback.test;

import org.springframework.stereotype.Component;
import tech.callback.swingspring.annotation.BindController;

@Component("testBean")
@BindController(controller = TestController.class)
public class TestSwingSpring
{

}

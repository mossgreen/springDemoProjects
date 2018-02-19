package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

//    @RequestMapping("/")
//    public String greeting (){
//        return "Greeting from Spring boot";
//    }

    @RequestMapping("/")
    public String index(ModelMap map) {
        map.addAttribute("host", "dispace.com");
        map.addAttribute("moss", "dispace.com");
        return "index";
    }
}

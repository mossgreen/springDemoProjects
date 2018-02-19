package hello;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController //shorthand for @Controller and  @ResponseBody rolled together.
public class GreetingController {
    private static final String template = "Hello, %s!";
    private static AtomicLong counter = new AtomicLong();

//    //@RequestMapping maps all HTTP operations by default, no need to specify get, post or else
//    @RequestMapping
//    public Greeting greeting(@RequestParam(value="name", required=true, defaultValue = "World") String name) {
//        System.out.println("=========in greeting ==========");
//        return new Greeting(counter.incrementAndGet(), String.format(template, name));
//    }

    //@RequestMapping maps all HTTP operations by default, no need to specify get, post or else
    @CrossOrigin(origins = "http://localhost:9000")
    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(required=true, defaultValue = "World") String name) {
        System.out.println("=========in greeting ==========");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/greeting-javaconfig")
    public Greeting greetingWithJavaConfig(@RequestParam(required = false, defaultValue = "Moss")String name) {
        System.out.println("========in greeting with java config=========");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}

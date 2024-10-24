package hexlet.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public final class WelcomeController {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring";
    }
}

package be.aplacetolive.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by medard on 16.06.17.
 */
@Controller
public class PublicPagesCtrl {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("home")
    public String home1(){
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }

    @GetMapping("403")
    public String error403() {
        return "error/403";
    }
}

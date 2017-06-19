package be.aplacetolive.contoller;

import be.aplacetolive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by medard on 16.06.17.
 */
@Controller
public class PublicPagesCtrl {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/home")
    public String home1(){
        return "index";
    }

    @GetMapping("about")
    public String about() {
        return "about";
    }

    @GetMapping("403")
    public String error403() {
        return "error/403";
    }

    @GetMapping("404")
    public String error404() {
        return "error/404";
    }
}

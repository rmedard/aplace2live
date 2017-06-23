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

    @GetMapping(value = "/")
    public String home(){
        return "index";
    }

    @GetMapping(value = "/home")
    public String home1(){
        return "index";
    }

    @GetMapping(value = "about")
    public String about() {
        return "about";
    }

    @GetMapping(value = "403")
    public String error403() {
        return "error/403";
    }

    @GetMapping(value = "404")
    public String error404() {
        return "error/404";
    }

    @GetMapping(value = "500")
    public String error500() {
        return "error/500";
    }
}

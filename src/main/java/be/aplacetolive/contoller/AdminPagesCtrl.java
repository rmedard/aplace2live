package be.aplacetolive.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by medard on 17.06.17.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminPagesCtrl {

    @GetMapping
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");
        return modelAndView;
    }
}

package be.aplacetolive.contoller;

import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ActiviteService activiteService;

    @GetMapping
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("activites", activiteService.getAllActivites());
        modelAndView.addObject("participants", userService.getAllParticipants());
        modelAndView.setViewName("admin/dashboard");
        return modelAndView;
    }
}

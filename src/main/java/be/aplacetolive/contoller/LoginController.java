package be.aplacetolive.contoller;

import be.aplacetolive.entity.User;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.LogginUtil;
import be.aplacetolive.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by medard on 18.06.17.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private LogginUtil logginUtil;

    @GetMapping(value = "/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = logginUtil.getLoggedInUser();
        if (loggedInUser == null){
            modelAndView.setViewName("login");
        } else {
            modelAndView.addObject("loggedInMsg", "Vous êtes déjà connecté!");
            modelAndView.addObject("user", loggedInUser);
            modelAndView.setViewName("redirect:currentprofile");
        }
        return modelAndView;
    }

    @PostMapping(value = "register")
    public ModelAndView createUser(@ModelAttribute("user") User user, BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null){
            bindingResult.rejectValue("email", "error.user", "Votre email est déjà enregistré");
        }

        if (!bindingResult.hasErrors()){
            userService.createUser(user);
            modelAndView.addObject("successMessage", "Le participant est enregistré");
            modelAndView.addObject("user", new User());
        }
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("typesParticipants", userService.getTypesParticipant());
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @GetMapping(value = "currentprofile")
    public ModelAndView findLoggedInUser(){
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = logginUtil.getLoggedInUser();
        if (loggedInUser != null){
            modelAndView.addObject("user", loggedInUser);
            modelAndView.addObject("typesParticipants", userService.getTypesParticipant());
            modelAndView.setViewName("users/profile");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }
}

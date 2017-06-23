package be.aplacetolive.contoller;

import be.aplacetolive.entity.User;
import be.aplacetolive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

    private Authentication auth;

    @GetMapping(value = "/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")){
            String email = auth.getName();
            User participant = userService.findUserByEmail(email);
            modelAndView.addObject("loggedInMsg", "Vous êtes déjà connecté!");
            modelAndView.addObject("user", participant);
            modelAndView.setViewName("redirect:currentprofile");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @PostMapping(value = "register")
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null){
            bindingResult.rejectValue("email", "error.user", "Votre email est déjà enregistré");
        }

        if (!bindingResult.hasErrors()){
            userService.createUser(user);
            modelAndView.addObject("successMsg", "Le participant est enregistré");
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
}

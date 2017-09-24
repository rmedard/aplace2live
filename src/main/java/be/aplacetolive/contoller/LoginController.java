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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by medard on 18.06.17.
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private LogginUtil logginUtil;

    @GetMapping
    public String home(){
        return "index";
    }

    @GetMapping(value = "/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = logginUtil.getLoggedInUser();
        if (loggedInUser == null){
            modelAndView.setViewName("login");
        } else {
            modelAndView.setViewName("redirect:/currentprofile");
        }
        return modelAndView;
    }

    @PostMapping(value = "register")
    public ModelAndView createUser(@ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        userValidator.validate(user, bindingResult);
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null){
            bindingResult.rejectValue("email", "error.user", "Votre email est déjà enregistré");
        }

        if (!bindingResult.hasErrors()){
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Le participant est enregistré");
        }
        return new ModelAndView("redirect:/register");
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
            modelAndView.setViewName("redirect:/participants?username=" + loggedInUser.getSlug());
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }
}

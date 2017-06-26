package be.aplacetolive.contoller;

import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.LogginUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
@RequestMapping(value = "participants")
public class ParticipantCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private LogginUtil logginUtil;

    @GetMapping
    public ModelAndView getAllParticipants(@RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "username", required = false) String slug){
        ModelAndView modelAndView = new ModelAndView();
        List<User> participants = new ArrayList<>();
        if (!StringUtils.isAnyEmpty(slug, type)){
            type = type.trim().toUpperCase();
            TypeParticipant typeParticipant = TypeParticipant.valueOf(type);
            User participant = userService.findUserBySlugAndType(slug, typeParticipant);
            participants.add(participant);
        } else {
            if (StringUtils.isAllEmpty(type, slug)){
                participants = userService.getAllParticipants();
            } else {
                if (StringUtils.isEmpty(type)){
                    User participant = userService.findUserBySlug(slug);
                    if (participant != null){
                        modelAndView.addObject("user", participant);
                        modelAndView.addObject("typesParticipants", userService.getTypesParticipant());
                        modelAndView.setViewName("users/profile");
                    } else {
                        modelAndView.setViewName("forward:/404");
                    }
                    return modelAndView;
                } else {
                    type = type.trim().toUpperCase();
                    TypeParticipant typeParticipant = TypeParticipant.valueOf(type);
                    participants = userService.getParticipantsByType(typeParticipant);
                }
            }
        }
        modelAndView.addObject("participants", participants);
        modelAndView.setViewName("users/default");
        return modelAndView;
    }

    @PatchMapping
    public ModelAndView updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        User loggedInUser = logginUtil.getLoggedInUser();
        if (loggedInUser == null){
            return new ModelAndView("login");
        } else {
            User userExists = userService.findUserById(user.getId());
            if (userExists == null){
                return new ModelAndView("forward:/404");
            } else {
                if (userExists.getId() != loggedInUser.getId()) {
                    return new ModelAndView("forward:/403");
                }
                User updatedUser = userService.updateUser(user);
                if (updatedUser != null){
                    redirectAttributes.addFlashAttribute("successMessage", "Le participant est mis à jour");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Mise à jour échouée!!");
                }
                return new ModelAndView("redirect:/currentprofile");
            }
        }
    }
}

package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
public class ParticipantCtrl {

    @Autowired
    private UserService userService;

    @GetMapping(value = "participants")
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
                        modelAndView.setViewName("users/profile");
                    } else {
                        modelAndView.setViewName("error/404");
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

    @GetMapping(value = "currentprofile")
    private ModelAndView findLoggedInUser(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()){
            String email = auth.getName();
            User participant = userService.findUserByEmail(email);
            if (participant != null) {
                modelAndView.addObject("user", participant);
                modelAndView.setViewName("users/profile");
            } else {
                modelAndView.setViewName("error/404");
            }
        } else {
            modelAndView.setViewName("home");
        }
        return modelAndView;
    }

    @PutMapping(value = "participants")
    public ModelAndView updateUser(@Valid User user, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserById(user.getId());
        if (userExists == null){
            bindingResult.rejectValue("email", "error.user", "L'utilisateur n'existe pas");
        }

        if (!bindingResult.hasErrors()){
            User updatedUser = userService.updateUser(user);
            modelAndView.addObject("successMessage", "Le participant est mis à jour");
            modelAndView.addObject("user", updatedUser);
            modelAndView.setViewName("users/profile");
        } else {
            modelAndView.setViewName("users/userform");
        }
        return modelAndView;
    }

    @GetMapping(value = "updateuser")
    public ModelAndView updateUser(@RequestParam(value = "username") String slug){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserBySlug(slug);
        if (user != null){
            modelAndView.addObject("user", user);
            modelAndView.addObject("typesParticipants", userService.getTypesParticipant());
            modelAndView.setViewName("users/userform");
        } else {
            modelAndView.setViewName("error/404");
        }
        return modelAndView;
    }

    @GetMapping(value = "participants/{slug}/activites")
    public ResponseEntity<List<Activite>> getParticipationsByParticipant(@PathVariable(value = "slug") String slug){
        User user = userService.findUserBySlug(slug);
        return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(new ArrayList<>(user.getActivites()), HttpStatus.OK);
    }

    @GetMapping(value = "types")
    public ResponseEntity<List<String>> getTypesParticipant(){
        return new ResponseEntity<>(userService.getTypesParticipant(), HttpStatus.OK);
    }
}

package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.Role;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.SlugUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
@RequestMapping("participants")
public class ParticipantCtrl {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllParticipants(@RequestParam(value = "type", required = false) String type){
        List<User> participants;
        if (!StringUtils.isEmpty(type)){
            String cleanType = type.trim().toUpperCase();
            try {
                TypeParticipant typeParticipant = TypeParticipant.valueOf(cleanType);
                participants = userService.getParticipantsByType(typeParticipant);
            } catch (IllegalArgumentException ex){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            participants = userService.getAllParticipants();
        }
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @GetMapping(value = "{slug}")
    public ModelAndView getUserBySlug(@PathVariable(value = "slug") String slug){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getUserBySlug(slug);
        if (user != null) {
            modelAndView.addObject("user", user);
            modelAndView.setViewName("user/profile");
        } else {
            modelAndView.setViewName("error/404");
        }
        return modelAndView;
    }

    @GetMapping(value = "{slug}/activites")
    public ResponseEntity<List<Activite>> getParticipationsByParticipant(@PathVariable(value = "slug") String slug){
        User user = userService.getUserBySlug(slug);
        return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(new ArrayList<>(user.getActivites()), HttpStatus.OK);
    }

    @GetMapping(value = "types")
    public ResponseEntity<List<String>> getTypesParticipant(){
        return new ResponseEntity<>(userService.getTypesParticipant(), HttpStatus.OK);
    }
}

package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.utils.LogginUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
@RequestMapping(value = "activites")
public class ActiviteCtrl {

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private LogginUtil logginUtil;

    @GetMapping
    public ModelAndView getAllActivites(@RequestParam(value = "type", required = false) String type){
        ModelAndView modelAndView = new ModelAndView();
        List<Activite> activites;
        if (!StringUtils.isEmpty(type)){
            type = type.trim().toUpperCase();
            try {
                TypeActivite typeActivite = TypeActivite.valueOf(type);
                activites = activiteService.getActivitesByType(typeActivite);
            } catch (IllegalArgumentException ex){
                activites = new ArrayList<>();
            }
        } else {
            activites = activiteService.getAllActivites();
        }

        if (logginUtil.getLoggedInUser() != null) {
            modelAndView.addObject("loggedInUser", logginUtil.getLoggedInUser());
        }

        modelAndView.addObject("activites", activites);
        modelAndView.setViewName("activites/default");
        return modelAndView;
    }

    @GetMapping(value = "{slug}")
    public ModelAndView getActiviteBySlug(@PathVariable(value = "slug") String slug){
        ModelAndView modelAndView = new ModelAndView();
        Activite activite = activiteService.getActiviteBySlug(slug);
        if (activite == null) {
            modelAndView.setViewName("error/404");
        } else {
            modelAndView.addObject("activite", activite);
            modelAndView.addObject("typesActivites", activiteService.getTypesActivite());
            modelAndView.setViewName("activites/activitePage");
        }
        return modelAndView;
    }

    @PostMapping(value = "add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView createActivite(Activite activite, ModelMap model){
        Activite act = activiteService.createActivite(activite);
        if (act == null) {
            model.addAttribute("errorMessage", "Sauvegarde échouée!");
        } else {
            model.addAttribute("successMessage", "L'activité est sauvegardée");
        }
        return new ModelAndView("redirect:/admin", model);
    }

    @PutMapping(value = "edit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView updateActivite(Activite activite, ModelMap model){
        Activite updated = activiteService.updateActivite(activite);
        if (updated == null) {
            return new ModelAndView("500");
        } else {
            model.addAttribute("activite", updated);
            model.addAttribute("successMessage", "L'activité est modifiée");
        }
        return new ModelAndView("redirect:/activites/" + updated.getSlug(), model);
    }

    @PostMapping(value = "/participant")
    public  ModelAndView addParticipant(@RequestBody String activiteSlug, ModelMap model){
        User loggedInUser = logginUtil.getLoggedInUser();
        if (loggedInUser != null){
            String slug = activiteSlug.substring(activiteSlug.indexOf("=") + 1);
            boolean participantAdded = activiteService.addParticipant(slug, loggedInUser.getId());
            if (participantAdded) {
                model.addAttribute("successMessage", "Participation enregistrée");
            } else {
                model.addAttribute("errorMessage", "Echec d'enregistrement");
            }
        }
        return new ModelAndView("redirect:/activites", model);
    }
}

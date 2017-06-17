package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.utils.SlugUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

    @GetMapping
    public String getAllActivites(@RequestParam(value = "type", required = false) String type, Model model){
        List<Activite> activites;
        if (!StringUtils.isEmpty(type)){
            String cleanType = type.trim().toUpperCase();
            try {
                TypeActivite typeActivite = TypeActivite.valueOf(cleanType);
                activites = activiteService.getActivitesByType(typeActivite);
            } catch (IllegalArgumentException ex){
                activites = new ArrayList<>();
            }
        } else {
            activites = activiteService.getAllActivites();
        }
        model.addAttribute("activites", activites);
        return "activites/index";
    }

    @GetMapping(value = "{slug}")
    public ResponseEntity<Activite> getActiviteBySlug(@PathVariable(value = "slug") String slug){
        Activite activite = activiteService.getActiviteBySlug(slug);
        return activite == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(activite, HttpStatus.OK);
    }

    @PostMapping(value = "add")
    public ResponseEntity<Void> createActivite(@RequestBody Activite activite, UriComponentsBuilder builder){
        activite.setSlug(SlugUtil.slugify(activite.getNom(), this.activiteService, SlugUtil.ACTIVITE));

        boolean flag = activiteService.createActivite(activite);
        if (flag == false) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/{id}").buildAndExpand(activite.getId()).toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "edit")
    public ResponseEntity<Void> updateActivite(@RequestBody Activite activite){
        boolean updated = activiteService.updateActivite(activite);
        return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "{slug}")
    public  ResponseEntity<Void> addParticipant(@PathVariable(value = "slug") String slug, @RequestParam(value = "participant") long participantId){
        boolean participantAdded = activiteService.addParticipant(slug, participantId);
        return participantAdded ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "types")
    public ResponseEntity<List<String>> getTypesActivite(){
        return new ResponseEntity<>(activiteService.getTypesActivite(), HttpStatus.OK);
    }
}

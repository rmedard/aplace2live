package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.utils.SlugUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
@RequestMapping("api/activites")
public class ActiviteCtrl {

    @Autowired
    private ActiviteService activiteService;

    @GetMapping
    public ResponseEntity<List<Activite>> getAllActivites(@RequestParam(value = "type", required = false) String type){
        List<Activite> activites;
        if (!StringUtils.isEmpty(type)){
            String cleanType = type.trim().toUpperCase();
            try {
                TypeActivite typeActivite = TypeActivite.valueOf(cleanType);
                activites = activiteService.getActivitesByType(typeActivite);
            } catch (IllegalArgumentException ex){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            activites = activiteService.getAllActivites();
        }
        return new ResponseEntity<>(activites, HttpStatus.OK);
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

    @PutMapping(value = "{slug}")
    public  ResponseEntity<Void> addParticipant(@RequestBody Participant participant, @PathVariable("slug") String slug){
        if (participant == null || participant.getId() == 0l){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            Activite activite = activiteService.getActiviteBySlug(slug);
            if (activite == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                activiteService.addParticipant(activite, participant);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }
}

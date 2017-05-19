package be.aplacetolive.contoller;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.service.ParticipantService;
import be.aplacetolive.utils.SlugUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Controller
@RequestMapping("api/participants")
public class ParticipantCtrl {

    @Autowired
    private ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants(@RequestParam(value = "type", required = false) String type){
        List<Participant> participants;
        if (!StringUtils.isEmpty(type)){
            String cleanType = type.trim().toUpperCase();
            try {
                TypeParticipant typeParticipant = TypeParticipant.valueOf(cleanType);
                participants = participantService.getParticipantsByType(typeParticipant);
            } catch (IllegalArgumentException ex){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            participants = participantService.getAllParticipants();
        }
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @PostMapping(value = "add")
    public ResponseEntity<Void> createParticipant(@RequestBody Participant participant, UriComponentsBuilder builder){
        participant.setSlug(SlugUtil.slugify(participant.getNom().concat(" " + participant.getPrenom()), this.participantService, SlugUtil.PARTICIPANT));

        boolean flag = participantService.createParticipant(participant);
        if (flag == false) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/{id}").buildAndExpand(participant.getId()).toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "{slug}")
    public ResponseEntity<Participant> getParticipantBySlug(@PathVariable(value = "slug") String slug){
        Participant participant = participantService.getParticipantBySlug(slug);
        return participant == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @GetMapping(value = "{slug}/activites")
    public ResponseEntity<List<Activite>> getParticipationsByParticipant(@PathVariable(value = "slug") String slug){
        Participant participant = participantService.getParticipantBySlug(slug);
        return participant == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(new ArrayList<>(participant.getActivites()), HttpStatus.OK);
    }
}

package be.aplacetolive.service;

import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeParticipant;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
public interface ParticipantService {
    Participant getParticipantBySlug(String slug);
    List<Participant> getAllParticipants();
    List<Participant> getParticipantsByType(TypeParticipant type);
    boolean createParticipant(Participant participant);
}

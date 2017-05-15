package be.aplacetolive.service;

import be.aplacetolive.entity.Participant;

/**
 * Created by Medard on 12/05/2017.
 */
public interface ParticipantService {
    Participant getParticipantBySlug(String slug);
}

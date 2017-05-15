package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Participant;
import be.aplacetolive.repository.ParticipantRepository;
import be.aplacetolive.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Medard on 12/05/2017.
 */
@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public Participant getParticipantBySlug(String slug) {
        return participantRepository.findParticipantBySlug(slug);
    }
}

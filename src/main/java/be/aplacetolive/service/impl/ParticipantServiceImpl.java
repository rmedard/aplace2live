package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.repository.ParticipantRepository;
import be.aplacetolive.service.ParticipantService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        CollectionUtils.addAll(participants, participantRepository.findAll().iterator());
        return participants;
    }

    @Override
    public List<Participant> getParticipantsByType(TypeParticipant type) {
        List<Participant> participants = new ArrayList<>();
        CollectionUtils.addAll(participants, participantRepository.findParticipantsByType(type).iterator());
        return participants;
    }

    @Override
    public boolean createParticipant(Participant participant) {
        Participant newParticipant = participantRepository.save(participant);
        return newParticipant == null ? false : true;
    }
}

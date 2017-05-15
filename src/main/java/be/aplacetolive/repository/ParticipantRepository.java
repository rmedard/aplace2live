package be.aplacetolive.repository;

import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeParticipant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    Participant findParticipantBySlug(String slug);
    List<Participant> findParticipantsByType(TypeParticipant type);
}

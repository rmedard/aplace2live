package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.Participant;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.repository.ActiviteRepository;
import be.aplacetolive.repository.ParticipantRepository;
import be.aplacetolive.service.ActiviteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Service
public class ActiviteServiceImpl implements ActiviteService {

    @Autowired
    private ActiviteRepository activiteRepo;

    @Autowired
    private ParticipantRepository participantRepo;

    @Override
    public List<Activite> getAllActivites() {
        List<Activite> activites = new ArrayList<>();
        CollectionUtils.addAll(activites, activiteRepo.findAll().iterator());
        return activites;
    }

    @Override
    public Activite getActiviteById(long id) {
        return null;
    }

    @Override
    public Activite getActiviteBySlug(String slug) {
        return activiteRepo.findActiviteBySlug(slug);
    }

    @Override
    public List<Activite> getActivitesByType(TypeActivite type) {
        List<Activite> activites = new ArrayList<>();
        CollectionUtils.addAll(activites, activiteRepo.findActiviteByType(type).iterator());
        return activites;
    }

    @Override
    public boolean createActivite(Activite activite) {
        Activite newActivite = activiteRepo.save(activite);
        return newActivite == null ? false : true;
    }

    @Override
    public boolean updateActivite(Activite activite) {
        if (activite == null || activite.getId() == 0L){
            return false;
        } else {
            Activite act = activiteRepo.findActiviteById(activite.getId());
            if (act == null){
                return false;
            } else {
                act.setDate(activite.getDate());
                act.setDescription(activite.getDescription());
                act.setLieu(activite.getLieu());
                act.setNom(activite.getNom());
                act.setType(activite.getType());
                activiteRepo.save(act);
                return true;
            }
        }
    }

    @Override
    public void deleteActivite(long id) {
    }

    @Override
    public boolean addParticipant(String activiteSlug, long participantId) {
        Activite activite = activiteRepo.findActiviteBySlug(activiteSlug);
        if (activite == null){
            return false;
        } else {
            Participant participant = participantRepo.findOne(participantId);
            if (participant == null){
                return false;
            } else {
                activite.getParticipants().add(participant);
                activiteRepo.save(activite);
                return true;
            }
        }
    }

    @Override
    public List<String> getTypesActivite() {
        List<String> types = new ArrayList<>();
        for (TypeActivite type : TypeActivite.values()){
            types.add(type.name());
        }
        return types;
    }
}

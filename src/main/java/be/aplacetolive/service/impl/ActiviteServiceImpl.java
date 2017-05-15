package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.repository.ActiviteRepository;
import be.aplacetolive.service.ActiviteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Service
public class ActiviteServiceImpl implements ActiviteService {

    @Autowired
    private ActiviteRepository activiteRepo;

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
    public boolean addActivite(Activite activite) {
        Activite newActivite = activiteRepo.save(activite);
        return newActivite == null ? false : true;
    }

    @Override
    public void updateActivite(Activite activite) {

    }

    @Override
    public void deleteActivite(long id) {

    }
}

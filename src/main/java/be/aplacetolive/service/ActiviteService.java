package be.aplacetolive.service;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.types.TypeActivite;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
public interface ActiviteService {
    List<Activite> getAllActivites();
    Activite getActiviteById(long id);
    Activite getActiviteBySlug(String slug);
    List<Activite> getActivitesByType(TypeActivite type);
    boolean addActivite(Activite activite);
    void updateActivite(Activite activite);
    void deleteActivite(long id);
}

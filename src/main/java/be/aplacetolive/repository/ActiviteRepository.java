package be.aplacetolive.repository;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.types.TypeActivite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
public interface ActiviteRepository extends CrudRepository<Activite, Long> {
    List<Activite> findActiviteByType(TypeActivite type);
    Activite findActiviteBySlug(String slug);
    Activite findActiviteById(Long id);
    void deleteActiviteBySlug(String slug);
}

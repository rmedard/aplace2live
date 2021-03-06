package be.aplacetolive.repository;

import be.aplacetolive.entity.Role;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findBySlug(String slug);
    User findByEmail(String email);
    User findBySlugAndType(String slug, TypeParticipant type);
    List<User> findUsersByType(TypeParticipant type);
    List<User> findUsersByRolesIs(Role role);
}

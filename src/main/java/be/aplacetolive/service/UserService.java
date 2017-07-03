package be.aplacetolive.service;

import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;

import java.util.List;

/**
 * Created by Medard on 12/05/2017.
 */
public interface UserService {
    User findUserBySlug(String slug);
    User findUserByEmail(String email);
    User findUserBySlugAndType(String slug, TypeParticipant type);
    User findUserById(Long id);
    User updateUser(User user);
    List<User> getAllParticipants();
    List<User> getParticipantsByType(TypeParticipant type);
    boolean createUser(User user);
    List<String> getTypesParticipant();
    String[] getActiveUsersEmails();
}

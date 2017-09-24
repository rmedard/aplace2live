package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Role;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.repository.RoleRepository;
import be.aplacetolive.repository.UserRepository;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Medard on 12/05/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserBySlug(String slug) {
        return userRepository.findBySlug(slug);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserBySlugAndType(String slug, TypeParticipant type) {
        return userRepository.findBySlugAndType(slug, type);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userRepository.findOne(user.getId());
        if (updatedUser == null){
            return null;
        } else {
            updatedUser.setActive(user.isActive());
            updatedUser.setNom(user.getNom());
            updatedUser.setPrenom(user.getPrenom());
            updatedUser.setType(user.getType());
            updatedUser.setEmail(user.getEmail());
            return userRepository.save(updatedUser);
        }
    }

    @Override
    public List<User> getAllParticipants() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getParticipantsByType(TypeParticipant type) {
        return userRepository.findUsersByType(type);
    }

    @Override
    public boolean createUser(User user) {
        user.setSlug(SlugUtil.slugify(user.getNom()
                .concat(" " + user.getPrenom()), this, SlugUtil.PARTICIPANT));
        user.setActive(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRole("ROLE_USER");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        User newUser = userRepository.save(user);
        return newUser != null;
    }

    @Override
    public List<String> getTypesParticipant() {
        List<String> types = new ArrayList<>();
        for (TypeParticipant type: TypeParticipant.values()){
            types.add(type.name());
        }
        return types;
    }

    @Override
    public String[] getActiveUsersEmails() {
        List<String> usersEmails = null;
        List<User> users = userRepository.findUsersByRolesIs(roleRepository.findByRole("ROLE_USER"));
        if (users != null) {
            usersEmails = new ArrayList<>();
            for (User user: users){
                usersEmails.add(user.getEmail());
            }
        }
        return usersEmails == null ? null : usersEmails.toArray(new String[0]);
    }
}

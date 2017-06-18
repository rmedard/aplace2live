package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Role;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeParticipant;
import be.aplacetolive.repository.RoleRepository;
import be.aplacetolive.repository.UserRepository;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.SlugUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
    public User getUserBySlug(String slug) {
        return userRepository.findBySlug(slug);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllParticipants() {
        List<User> participants = new ArrayList<>();
        CollectionUtils.addAll(participants, userRepository.findAll().iterator());
        return participants;
    }

    @Override
    public List<User> getParticipantsByType(TypeParticipant type) {
        List<User> participants = new ArrayList<>();
        CollectionUtils.addAll(participants, userRepository.findUsersByType(type).iterator());
        return participants;
    }

    @Override
    public boolean createUser(User user) {
        user.setSlug(SlugUtil.slugify(user.getNom()
                .concat(" " + user.getPrenom()), this, SlugUtil.PARTICIPANT));
        user.setActive(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(role)));
        User newUser = userRepository.save(user);
        return newUser == null ? false : true;
    }

    @Override
    public List<String> getTypesParticipant() {
        List<String> types = new ArrayList<>();
        for (TypeParticipant type: TypeParticipant.values()){
            types.add(type.name());
        }
        return types;
    }
}

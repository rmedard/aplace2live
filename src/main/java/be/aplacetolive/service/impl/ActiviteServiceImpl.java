package be.aplacetolive.service.impl;

import be.aplacetolive.entity.Activite;
import be.aplacetolive.entity.User;
import be.aplacetolive.entity.types.TypeActivite;
import be.aplacetolive.repository.ActiviteRepository;
import be.aplacetolive.repository.UserRepository;
import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.service.EmailService;
import be.aplacetolive.service.UserService;
import be.aplacetolive.utils.SlugUtil;
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

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

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
    public Activite createActivite(Activite activite) {
        activite.setSlug(SlugUtil.slugify(activite.getNom(), this, SlugUtil.ACTIVITE));
        Activite newActivite = activiteRepo.save(activite);
        if (newActivite != null){
            String[] emails = userService.getActiveUsersEmails();
            if (emails != null || emails.length > 0) {
                String emailBody = "Une nouvelle activité a été créée: <a href=\"www.aplace2live.tk/activites/" +
                        newActivite.getSlug() + "\">" + newActivite.getNom() + "</a>";
                emailService.sendSimpleMessage(emails, emailBody, "Une nouvelle activité créée");
            }
        }
        return newActivite;
    }

    @Override
    public Activite updateActivite(Activite activite) {
        if (activite == null || activite.getId() == 0L){
            return null;
        } else {
            Activite act = activiteRepo.findActiviteById(activite.getId());
            if (act == null){
                return null;
            } else {
                act.setDate(activite.getDate());
                act.setDescription(activite.getDescription());
                act.setLieu(activite.getLieu());
                act.setNom(activite.getNom());
                act.setType(activite.getType());
                activiteRepo.save(act);
                return act;
            }
        }
    }

    @Override
    public void deleteActivite(long id) {
    }

    @Override
    public boolean addParticipant(String activiteSlug, long userId) {
        Activite activite = activiteRepo.findActiviteBySlug(activiteSlug);
        if (activite == null){
            return false;
        } else {
            User participant = userRepo.findOne(userId);
            if (participant == null){
                return false;
            } else {
                activite.getUsers().add(participant);
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

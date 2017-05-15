package be.aplacetolive.utils;

import be.aplacetolive.service.ActiviteService;
import be.aplacetolive.service.ParticipantService;
import com.github.slugify.Slugify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Medard on 14/05/2017.
 */
public class SlugUtil {

    public static final String ACTIVITE = "activite";
    public static final String PARTICIPANT = "participant";

    public static String slugify(String chars, Object service, String typeModel){
        Slugify slugify = new Slugify();
        String slug = null;
        if (!StringUtils.isEmpty(chars)){
            slug = new SlugUtil().checkValidite(slugify.slugify(chars), service, typeModel);
        }
        return slug;
    }

    private String checkValidite(String slug, Object service, String typeModel){
        int extension = 1;
        String baseSlug = slug;
        while (true){
            if (typeModel.equals(ACTIVITE)){
                if (((ActiviteService)service).getActiviteBySlug(slug) != null){
                    slug = baseSlug.concat("-" + extension);
                    extension++;
                    continue;
                } break;
            } else if (typeModel.equals(PARTICIPANT)){
                if (((ParticipantService)service).getParticipantBySlug(slug) != null){
                    slug = baseSlug.concat("-" + extension);
                    extension++;
                    continue;
                } break;
            }
        }
        return slug;
    }

}

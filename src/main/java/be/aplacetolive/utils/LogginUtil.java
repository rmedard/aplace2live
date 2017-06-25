package be.aplacetolive.utils;

import be.aplacetolive.entity.User;
import be.aplacetolive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by medard on 25.06.17.
 */
@Component
public class LogginUtil {

    @Autowired
    private UserService userService;

    public User getLoggedInUser(){
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")){
            user = userService.findUserByEmail(auth.getName());
        }
        return user;
    }
}

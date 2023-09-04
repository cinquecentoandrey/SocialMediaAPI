package com.cinquecento.smapi.util;

import com.cinquecento.smapi.model.User;
import com.cinquecento.smapi.security.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserInfo {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}

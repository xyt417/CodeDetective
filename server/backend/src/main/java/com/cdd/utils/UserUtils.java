package com.cdd.utils;

import com.cdd.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static User getUserFromContext() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
}

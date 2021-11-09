package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * User dto contains only userEmail(aka login) and list of authorities names
 */
@Getter
@AllArgsConstructor
public class UserLoginAndRolesDTO {
    private final String userEmail;
    private final List<String> userRoleNames;

    /**
     * Constructor from Authentication
     *
     * @param authentication - Authentication with AuthenticatedJwtToken
     */
    public UserLoginAndRolesDTO(final Authentication authentication) {
        Object principalAsLogin = authentication.getPrincipal();
        if (principalAsLogin instanceof UserDetails) {
            this.userEmail = ((UserDetails) principalAsLogin).getUsername();
        } else {
            this.userEmail = principalAsLogin.toString();
        }

        this.userRoleNames = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            this.userRoleNames.add(grantedAuthority.getAuthority());
        }
    }
}

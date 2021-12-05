package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.enumerations.ERoleName;
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
public class UserEmailAndRolesDTO {
    private final String email;
    private final List<ERoleName> roles;

    /**
     * Constructor from Authentication
     *
     * @param authentication - Authentication with AuthenticatedJwtToken
     */
    public UserEmailAndRolesDTO(final Authentication authentication) {
        Object principalAsLogin = authentication.getPrincipal();
        if (principalAsLogin instanceof UserDetails) {
            this.email = ((UserDetails) principalAsLogin).getUsername();
        } else {
            this.email = principalAsLogin.toString();
        }

        this.roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            this.roles.add(ERoleName.valueOf(grantedAuthority.getAuthority()));
        }
    }

    public List<String> getUserRoleNames() {
        List<String> stringList = new ArrayList<>(roles.size());
        for(ERoleName roleName: roles){
            stringList.add(roleName.toString());
        }
        return stringList;
    }
}

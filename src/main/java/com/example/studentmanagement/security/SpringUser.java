package com.example.studentmanagement.security;

import com.example.studentmanagement.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
@Getter
public class SpringUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public  SpringUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getUserType().name()));
        this.user = user;
    }
}
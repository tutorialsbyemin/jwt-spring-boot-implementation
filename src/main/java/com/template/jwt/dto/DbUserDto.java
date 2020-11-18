package com.template.jwt.dto;

import com.template.jwt.entity.DbUser;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DbUserDto {
    private long id;
    private String username;
    private String password;
    private Set<DbRoleDto> roles = new HashSet<>();

    public DbUserDto(DbUser dbUser) {
        this.id = dbUser.getId();
        this.username = dbUser.getUsername();
        this.password = dbUser.getPassword();
        dbUser.getRoles().forEach(role -> roles.add(new DbRoleDto(role)));
    }
}

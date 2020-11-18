package com.template.jwt.dto;

import com.template.jwt.entity.DbRole;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DbRoleDto {
    private long id;
    private String name;

    public DbRoleDto(DbRole dbRole) {
        this.id = dbRole.getId();
        this.name = dbRole.getName();
    }
}

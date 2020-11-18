package com.template.jwt.dto;

import com.template.jwt.entity.DbRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DbRoleDto {
    private long id;
    private String name;

    public DbRoleDto(DbRole dbRole) {
        this.id = dbRole.getId();
        this.name = dbRole.getName();
    }
}

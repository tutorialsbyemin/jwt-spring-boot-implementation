package com.template.jwt.repository;

import com.template.jwt.entity.DbRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbRoleRepository extends JpaRepository<DbRole, Long> {
}

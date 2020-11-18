package com.template.jwt.service;

import com.template.jwt.dto.DbUserDto;
import com.template.jwt.repository.DbUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DbUserService {
    private final DbUserRepository dbUserRepository;

    public Optional<DbUserDto> findByUsername(String username) {
        return dbUserRepository.findByUsername(username).map(DbUserDto::new);
    }

    public Optional<DbUserDto> findById(long id) {
        return dbUserRepository.findById(id).map(DbUserDto::new);
    }

}

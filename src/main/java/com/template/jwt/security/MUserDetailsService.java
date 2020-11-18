package com.template.jwt.security;

import com.template.jwt.dto.DbUserDto;
import com.template.jwt.service.DbUserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Log4j2
@Configuration
@AllArgsConstructor
public class MUserDetailsService implements UserDetailsService {
    private final DbUserService dbUserService;

    public static UserDetails map(DbUserDto dbUserDto) {
        return new MUserDetails(dbUserDto.getId(), dbUserDto.getUsername(), dbUserDto.getPassword(), dbUserDto.getRoles());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dbUserService.findByUsername(username)
                .map(MUserDetailsService::map)
                .orElseThrow(() -> {
                    String msg = String.format("User `%s` is not found in the database", username);
                    log.warn(msg);
                    return new UsernameNotFoundException(msg);
                });
    }

    public UserDetails loadUserById(long userId) throws UsernameNotFoundException {
        return dbUserService.findById(userId)
                .map(MUserDetailsService::map)
                .orElseThrow(() -> {
                    String msg = String.format("User with id `%d` is not found in the database", userId);
                    log.warn(msg);
                    return new UsernameNotFoundException(msg);
                });
    }

}

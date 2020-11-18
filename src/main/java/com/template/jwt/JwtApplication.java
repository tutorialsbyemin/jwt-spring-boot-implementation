package com.template.jwt;

import com.template.jwt.entity.DbRole;
import com.template.jwt.entity.DbUser;
import com.template.jwt.repository.DbRoleRepository;
import com.template.jwt.repository.DbUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

    @Bean
    CommandLineRunner createInitialData(DbUserRepository userRepository, DbRoleRepository roleRepository) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return args -> {
            DbUser johnprice = new DbUser("johnprice", encoder.encode("12345"));
            DbUser simonriley = new DbUser("simonriley", encoder.encode("54321"));
            DbUser johnmactavish = new DbUser("johnmactavish", encoder.encode("00000"));

            DbRole admin = new DbRole("ROLE_ADMIN");
            DbRole user = new DbRole("ROLE_USER");

            userRepository.saveAll(Arrays.asList(johnprice, simonriley, johnmactavish));
            roleRepository.saveAll(Arrays.asList(admin, user));

            Set<DbRole> priceRoles = new HashSet<>();
            Set<DbRole> ghostRoles = new HashSet<>();
            Set<DbRole> soapRoles = new HashSet<>();

            priceRoles.add(admin);
            ghostRoles.add(user);
            soapRoles.add(user);

            johnprice.setRoles(priceRoles);
            simonriley.setRoles(ghostRoles);
            johnmactavish.setRoles(soapRoles);

            userRepository.saveAll(Arrays.asList(johnprice, simonriley, johnmactavish));

        };

    }

}

package com.foodygo.initdb;

import com.foodygo.enums.EnumRoleName;
import com.foodygo.pojos.Role;
import com.foodygo.pojos.User;
import com.foodygo.repositories.RoleRepository;
import com.foodygo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public CommandLineRunner database() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (roleRepository.count() == 0) {
                    Role roleAdmin = new Role(EnumRoleName.ROLE_ADMIN, null);
                    Role roleStaff = new Role(EnumRoleName.ROLE_STAFF, null);
                    Role roleUser = new Role(EnumRoleName.ROLE_USER, null);
                    Role roleManager = new Role(EnumRoleName.ROLE_MANAGER, null);

                    roleRepository.save(roleAdmin);
                    roleRepository.save(roleStaff);
                    roleRepository.save(roleUser);
                    roleRepository.save(roleManager);
                }

                if (userRepository.count() == 0) {
                    Role role = roleRepository.getRoleByRoleName(EnumRoleName.ROLE_USER);
                    User user = User.builder()
                            .firstName("Hoang")
                            .lastName("Ha")
                            .accessToken(null)
                            .refreshToken(null)
                            .email("hoangsonhadev@gmail.com")
                            .password(bCryptPasswordEncoder.encode("1"))
                            .enabled(true)
                            .nonLocked(true)
                            .role(role)
                            .codeVerify("3333")
                            .build();
                    userRepository.save(user);
                }
            }
        };
    }
}

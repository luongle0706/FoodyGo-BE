package com.foodygo.initdb;

import com.foodygo.entity.Role;
import com.foodygo.entity.User;
import com.foodygo.enums.EnumRoleName;
import com.foodygo.repository.RoleRepository;
import com.foodygo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public CommandLineRunner database() {
        return args -> {
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
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(role)
                        .codeVerify("3333")
                        .build();
                userRepository.save(user);
            }
        };
    }
}

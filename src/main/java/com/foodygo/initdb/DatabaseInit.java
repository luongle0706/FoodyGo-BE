package com.foodygo.initdb;

import com.foodygo.entity.Category;
import com.foodygo.entity.Restaurant;
import com.foodygo.entity.Role;
import com.foodygo.entity.User;
import com.foodygo.enums.EnumRoleName;
import com.foodygo.repository.CategoryRepository;
import com.foodygo.repository.RestaurantRepository;
import com.foodygo.repository.RoleRepository;
import com.foodygo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

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
                        .fullName("HOANG SON HA")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("hoangsonhadev@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(role)
                        .build();
                userRepository.save(user);
            }

            Random random = new Random();

            if (restaurantRepository.count() <= 0) {
                for (int i = 0; i < 10; i++) {
                    Restaurant restaurant = Restaurant.builder()
                            .name("Restaurant " + i)
                            .phone("+84" + (100000000 + random.nextInt(900000000)))
                            .email("restaurant" + i + "@foodygo.com")
                            .address((1 + random.nextInt(100)) + "/" + (1 + random.nextInt(100)) + " Street " + (1 + random.nextInt(100)))
                            .image("restaurant" + i + ".png")
                            .build();
                    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

                    for (int j = 0; j < 10; j++) {
                        Category category = Category.builder()
                                .name("R" + restaurant.getId() + "C" + j)
                                .description("Sample category no. " + j + " from restaurant " + restaurant.getId())
                                .restaurant(savedRestaurant)
                                .build();
                        categoryRepository.save(category);
                    }
                }
            }
        };
    }
}

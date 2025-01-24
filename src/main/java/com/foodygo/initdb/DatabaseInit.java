package com.foodygo.initdb;

import com.foodygo.entity.*;
import com.foodygo.repository.*;
import com.foodygo.enums.EnumRoleNameType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final HubRepository hubRepository;
    private final BuildingRepository buildingRepository;

    @Bean
    public CommandLineRunner database(CustomerRepository customerRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role roleAdmin = new Role(EnumRoleNameType.ROLE_ADMIN, null);
                Role roleStaff = new Role(EnumRoleNameType.ROLE_STAFF, null);
                Role roleUser = new Role(EnumRoleNameType.ROLE_USER, null);
                Role roleManager = new Role(EnumRoleNameType.ROLE_MANAGER, null);
                Role roleSeller = new Role(EnumRoleNameType.ROLE_SELLER, null);

                roleRepository.save(roleAdmin);
                roleRepository.save(roleStaff);
                roleRepository.save(roleUser);
                roleRepository.save(roleManager);
                roleRepository.save(roleSeller);
            }

            if (userRepository.count() == 0) {
                Role roleUser = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
                Role roleAdmin = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
                Role roleStaff = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_STAFF);
                Role roleManager = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_MANAGER);
                Role roleSeller = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_SELLER);

                User user = User.builder()
                        .fullName("User")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("user@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleUser)
                        .build();
                userRepository.save(user);

                User admin = User.builder()
                        .fullName("Admin")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("admin@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleAdmin)
                        .build();
                userRepository.save(admin);

                User manager = User.builder()
                        .fullName("Manager")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("manager@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleManager)
                        .build();
                userRepository.save(manager);

                User staff = User.builder()
                        .fullName("HOANG SON HA")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("staff@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleStaff)
                        .build();
                userRepository.save(staff);

                User seller = User.builder()
                        .fullName("Seller")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("seller@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleSeller)
                        .build();
                userRepository.save(seller);
            }

            if (hubRepository.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Hub hub = Hub.builder()
                            .address("Hub Address " + i)
                            .name("Hub " + i)
                            .description("Hub Description " + i)
                            .build();
                    Hub savedHub = hubRepository.save(hub);

                    for (int j = 1; j <= 5; j++) {
                        Building building = Building.builder()
                                .name("Building " + j + " in hub " + i)
                                .description("Building Description " + j)
                                .hub(savedHub)
                                .build();
                        buildingRepository.save(building);
                    }
                }
            }

            if (customerRepository.count() == 0) {

                List<User> userList = userRepository.findAll();

                Building building = buildingRepository.findBuildingById(1);

                for (int i = 0; i < 5 && i < userList.size(); i++) {
                    User user = userList.get(i);

                    Customer customer = Customer.builder()
                            .building(building)
                            .user(user)
                            .build();
                    customerRepository.save(customer);
                }
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

                        Product product = Product.builder()
                                .code("R" + restaurant.getId() + "P" + j)
                                .name("Sample product no. " + j)
                                .price(Math.random() * 50 + 1)
                                .description("Sample product no. " + j + " from restaurant " + restaurant.getId())
                                .prepareTime(Math.random() * 50 + 1)
                                .restaurant(restaurant)
                                .category(category)
                                .build();
                        productRepository.save(product);
                    }
                }
            }
        };
    }
}

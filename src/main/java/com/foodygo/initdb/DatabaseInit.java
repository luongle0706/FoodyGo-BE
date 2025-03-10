package com.foodygo.initdb;

import com.foodygo.entity.*;
import com.foodygo.enums.EnumRoleNameType;
import com.foodygo.enums.OrderStatus;
import com.foodygo.enums.WalletType;
import com.foodygo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DatabaseInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AddonSectionRepository addonSectionRepository;
    private final AddonItemRepository addonItemRepository;
    private final HubRepository hubRepository;
    private final BuildingRepository buildingRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WalletRepository walletRepository;

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

            User user = null, admin, manager, staff = null, seller;
            if (userRepository.count() == 0) {
                Role roleUser = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
                Role roleAdmin = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
                Role roleStaff = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_STAFF);
                Role roleManager = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_MANAGER);
                Role roleSeller = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_SELLER);

                user = User.builder()
                        .fullName("User")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("user@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleUser)
                        .build();
                user = userRepository.save(user);

                admin = User.builder()
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

                manager = User.builder()
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

                staff = User.builder()
                        .fullName("HOANG SON HA")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("staff@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleStaff)
                        .build();
                staff = userRepository.save(staff);

                seller = User.builder()
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

            Customer customer = null;
            if (customerRepository.count() == 0) {

                Building building = buildingRepository.findBuildingById(1);

                customer = Customer.builder()
                        .building(building)
                        .user(user)
                        .build();
                customer = customerRepository.save(customer);

                Wallet wallet = Wallet.builder()
                        .balance(0.0)
                        .walletType(WalletType.CUSTOMER)
                        .customer(customer)
                        .build();
                walletRepository.save(wallet);
            }

            Random random = new Random();

            Restaurant restaurant = null;
            if (restaurantRepository.count() <= 0) {
                User owner = userRepository.findById(5).orElseThrow(
                        () -> new RuntimeException("Owner not found when init restaurant")
                );
                restaurant = Restaurant.builder()
                        .name("Cơm tấm Ngô Quyền")
                        .phone("+84" + (100000000 + random.nextInt(900000000)))
                        .email("restaurant@foodygo.com")
                        .owner(owner)
                        .address((1 + random.nextInt(100)) + "/" + (1 + random.nextInt(100)) + " Street " + (1 + random.nextInt(100)))
                        .image("https://img-global.cpcdn.com/recipes/49876fe80303b991/640x640sq70/photo.webp")
                        .build();

                restaurant = restaurantRepository.save(restaurant);

                Wallet sellerWallet = Wallet.builder()
                        .balance(0.0)
                        .walletType(WalletType.RESTAURANT)
                        .restaurant(restaurant)
                        .build();
                walletRepository.save(sellerWallet);

                for (int j = 0; j < 10; j++) {
                    Category category = Category.builder()
                            .name("R" + restaurant.getId() + "C" + j)
                            .description("Sample category no. " + j + " from restaurant " + restaurant.getId())
                            .restaurant(restaurant)
                            .build();
                    categoryRepository.save(category);

                    int randomPrice = ThreadLocalRandom.current().nextInt(10, 100);

                    Product product = Product.builder()
                            .code("R" + restaurant.getId() + "P" + j)
                            .name("Sample product no. " + j)
                            .price(randomPrice * 1000.0)
                            .description("Sample product no. " + j + " from restaurant " + restaurant.getId())
                            .prepareTime(ThreadLocalRandom.current().nextDouble(10, 120))
                            .restaurant(restaurant)
                            .category(category)
                            .build();
                    productRepository.save(product);

                    AddonSection addonSection = AddonSection.builder()
                            .name("Sample addon section no." + j)
                            .maxChoice(((int) (Math.random() * 3)) + 1)
                            .product(product)
                            .build();
                    addonSectionRepository.save(addonSection);

                    for (int k = 0; k < 3; k++) {
                        int randomAddonItemPrice = ThreadLocalRandom.current().nextInt(0, 10);
                        AddonItem addonItem = AddonItem.builder()
                                .name("Sample addon item no." + k)
                                .price(randomAddonItemPrice * 1000.0)
                                .quantity(ThreadLocalRandom.current().nextInt(1, 10))
                                .section(addonSection)
                                .build();
                        addonItemRepository.save(addonItem);
                    }
                }
            }

            if (orderRepository.count() == 0) {
                List<Hub> hubs = hubRepository.findAll();

                // Phí dịch vụ & vận chuyển
                double shippingFee = random.nextDouble() * 5 + 2;
                double serviceFee = random.nextDouble() * 3 + 1;
                double totalPrice = 0;

                // Tạo đơn hàng
                for (Hub hub : hubs) {
                    Order order = Order.builder()
                            .time(LocalDateTime.now().minusDays(random.nextInt(10)))
                            .shippingFee(shippingFee)
                            .serviceFee(serviceFee)
                            .totalPrice(totalPrice + shippingFee + serviceFee)
                            .status(OrderStatus.ORDERED)
                            .expectedDeliveryTime(LocalDateTime.now().plusHours(random.nextInt(5) + 1))
                            .customerPhone(user != null ? user.getPhone() : "190238019283")
                            .shipperPhone("+84" + (100000000 + random.nextInt(900000000)))
                            .notes("Giao hàng trước 6h tối")
                            .employee(staff)
                            .customer(customer)
                            .restaurant(restaurant)
                            .hub(hub)
                            .build();
                    orderRepository.save(order);
                }
            }

            if (orderDetailRepository.count() == 0) {
                List<Order> orders = orderRepository.findAll();
                Product product1 = productRepository.findById(1).orElseThrow();
                Product product2 = productRepository.findById(2).orElseThrow();

                for (Order order : orders) {
                    OrderDetail orderDetail1 = OrderDetail.builder()
                            .order(order)
                            .product(product1)
                            .price(product1.getPrice())
                            .quantity(1)
                            .build();
                    OrderDetail orderDetail2 = OrderDetail.builder()
                            .order(order)
                            .product(product2)
                            .price(product2.getPrice())
                            .quantity(1)
                            .build();
                    orderDetailRepository.save(orderDetail1);
                    orderDetailRepository.save(orderDetail2);
                }
            }
        };
    }
}

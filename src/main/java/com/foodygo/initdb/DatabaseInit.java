package com.foodygo.initdb;

import com.foodygo.entity.*;
import com.foodygo.enums.OrderStatus;
import com.foodygo.enums.WalletType;
import com.foodygo.repository.*;
import com.foodygo.enums.EnumRoleNameType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final AddonSectionRepository addonSectionRepository;
    private final AddonItemRepository addonItemRepository;
    private final HubRepository hubRepository;
    private final BuildingRepository buildingRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderActivityRepository orderActivityRepository;
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

            if (customerRepository.count() == 0 && walletRepository.count() == 0) {

                User user = userRepository.findById(1).orElseThrow(
                        () -> new RuntimeException("User not found when creating customer")
                );
                Building building = buildingRepository.findBuildingById(1);

                Customer customer = Customer.builder()
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

            if (restaurantRepository.count() <= 0 && walletRepository.count() <= 0) {
                User owner = userRepository.findById(5).orElseThrow(
                        () -> new RuntimeException("Owner not found when init restaurant")
                );
                Restaurant restaurant = Restaurant.builder()
                        .name("Cơm tấm Ngô Quyền")
                        .phone("+84" + (100000000 + random.nextInt(900000000)))
                        .email("restaurant@foodygo.com")
                        .owner(owner)
                        .address((1 + random.nextInt(100)) + "/" + (1 + random.nextInt(100)) + " Street " + (1 + random.nextInt(100)))
                        .image("https://img-global.cpcdn.com/recipes/49876fe80303b991/640x640sq70/photo.webp")
                        .build();

                Restaurant savedRestaurant = restaurantRepository.save(restaurant);

                Wallet staffWallet = Wallet.builder()
                        .balance(0.0)
                        .walletType(WalletType.RESTAURANT)
                        .restaurant(savedRestaurant)
                        .build();
                walletRepository.save(staffWallet);

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

                    AddonSection addonSection = AddonSection.builder()
                            .name("Sample addon section no." + j)
                            .maxChoice(((int) (Math.random() * 3)) + 1)
                            .product(product)
                            .build();
                    addonSectionRepository.save(addonSection);

                    for (int k = 0; k < 3; k++) {
                        AddonItem addonItem = AddonItem.builder()
                                .name("Sample addon item no." + k)
                                .price(Math.random() * 5 + 1)
                                .quantity(100)
                                .section(addonSection)
                                .build();
                        addonItemRepository.save(addonItem);
                    }
                }
            }

//            if (orderRepository.count() == 0) {
//                List<User> employees = userRepository.findAll();
//                List<Customer> customers = customerRepository.findAll();
//                List<Restaurant> restaurants = restaurantRepository.findAll();
//                List<Hub> hubs = hubRepository.findAll();
//
//                for (int i = 0; i < 20; i++) {
//                    User randomEmployee = employees.get(random.nextInt(employees.size()));
//                    Customer randomCustomer = customers.get(random.nextInt(customers.size()));
//                    Restaurant randomRestaurant = restaurants.get(random.nextInt(restaurants.size()));
//                    Hub randomHub = hubs.get(random.nextInt(hubs.size()));
//                    List<Product> products = productRepository.findByRestaurantIdAndDeletedFalse(randomRestaurant.getId());
//
//                    // Phí dịch vụ & vận chuyển
//                    double shippingFee = random.nextDouble() * 5 + 2;
//                    double serviceFee = random.nextDouble() * 3 + 1;
//                    double totalPrice = 0;
//
//                    // Danh sách OrderDetail
//                    List<OrderDetail> orderDetails = new ArrayList<>();
//                    for (int j = 0; j < random.nextInt(5) + 1; j++) {
//                        Product randomProduct = products.get(random.nextInt(products.size()));
//                        int quantity = random.nextInt(3) + 1;
//                        double price = randomProduct.getPrice() * quantity;
//                        totalPrice += price;
//
//                        OrderDetail orderDetail = OrderDetail.builder()
//                                .product(randomProduct)
//                                .quantity(quantity)
//                                .price(price)
//                                .addonItems("Addon " + j)
//                                .order(null) // Gán sau khi tạo order
//                                .build();
//                        orderDetails.add(orderDetail);
//                    }
//
//                    // Tạo đơn hàng
//                    Order order = Order.builder()
//                            .time(LocalDateTime.now().minusDays(random.nextInt(10)))
//                            .shippingFee(shippingFee)
//                            .serviceFee(serviceFee)
//                            .totalPrice(totalPrice + shippingFee + serviceFee)
//                            .status(OrderStatus.ORDERED)
//                            .expectedDeliveryTime(LocalDateTime.now().plusHours(random.nextInt(5) + 1))
//                            .customerPhone(randomCustomer.getUser().getPhone())
//                            .shipperPhone("+84" + (100000000 + random.nextInt(900000000)))
//                            .notes("Giao hàng trước 6h tối")
//                            .employee(randomEmployee)
//                            .customer(randomCustomer)
//                            .restaurant(randomRestaurant)
//                            .hub(randomHub)
//                            .build();
//                    orderRepository.save(order);
//
//                    // Gán order cho OrderDetail và lưu
//                    for (OrderDetail detail : orderDetails) {
//                        detail.setOrder(order);
//                        orderDetailRepository.save(detail);
//                    }
//
//                    // Tạo lịch sử trạng thái OrderActivity
//                    List<OrderActivity> orderActivities = new ArrayList<>();
//                    OrderStatus[] statuses = OrderStatus.values();
//                    for (int k = 0; k < random.nextInt(4) + 1; k++) {
//                        OrderStatus fromStatus = statuses[random.nextInt(statuses.length)];
//                        OrderStatus toStatus = statuses[random.nextInt(statuses.length)];
//
//                        OrderActivity orderActivity = OrderActivity.builder()
//                                .fromStatus(fromStatus)
//                                .toStatus(toStatus)
//                                .time(order.getTime().plusMinutes(k * 15))
//                                .image("order_activity_" + k + ".png")
//                                .user(randomEmployee)
//                                .order(order)
//                                .build();
//                        orderActivities.add(orderActivity);
//                    }
//                    orderActivityRepository.saveAll(orderActivities);
//                }
//            }
        };
    }
}

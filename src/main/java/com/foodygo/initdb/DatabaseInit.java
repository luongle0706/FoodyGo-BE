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

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
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
                Random random = new Random();
                double[] hubLatitudes = new double[] {10.883272, 10.883719, 10.883246, 10.881893, 10.881890, 10.882405, 10.882813, 10.883466, 10.884012, 10.884505, 10.885257};
                double[] hubLongitudes = new double[] {106.783463, 106.780424, 106.779642, 106.781007, 106.781691, 106.781505, 106.782802, 106.779903, 106.782519, 106.781359, 106.782090};
                // Danh sách ánh xạ Hub → Buildings
                Map<String, String[]> hubBuildingMap = new HashMap<>();
                hubBuildingMap.put("Hub 1", new String[]{"A1", "A2", "A3"});
                hubBuildingMap.put("Hub 2", new String[]{"A4", "A5"});
                hubBuildingMap.put("Hub 3", new String[]{"B1", "B2", "B3"});
                hubBuildingMap.put("Hub 4", new String[]{"B4", "B5"});
                hubBuildingMap.put("Hub 5", new String[]{"C1", "C2"});
                hubBuildingMap.put("Hub 6", new String[]{"C3", "C4"});
                hubBuildingMap.put("Hub 7", new String[]{"C5", "C6"});
                hubBuildingMap.put("Hub 8", new String[]{"D2", "D3", "D4"});
                hubBuildingMap.put("Hub 9", new String[]{"D5", "D6"});
                hubBuildingMap.put("Hub 10", new String[]{"E1"});
                hubBuildingMap.put("Hub 11", new String[]{"G1"});
                hubBuildingMap.put("Hub 12", new String[]{"F1", "F2"});

                // Tạo danh sách tọa độ building
                Map<String, double[]> buildingCoordinates = new HashMap<>();
                String[] buildingNames = {
                        "A1", "A2", "A3", "A4", "A5",
                        "B1", "B2", "B3", "B4", "B5",
                        "C1", "C2", "C3", "C4", "C5", "C6",
                        "D2", "D3", "D4", "D5", "D6",
                        "E1", "G1", "F1", "F2"
                };
                double[] buildingLatitudes = {
                        10.881837, 10.882055, 10.882527, 10.881701, 10.882287,
                        10.882730, 10.883051, 10.883491, 10.883172, 10.883874,
                        10.883195, 10.883517, 10.883755, 10.884056, 10.883054, 10.883545,
                        10.884469, 10.884741, 10.884957, 10.884909, 10.885382,
                        10.884249, 10.885634, 10.885450, 10.885655
                };
                double[] buildingLongitudes = {
                        106.781889, 106.781678, 106.781222, 106.781364, 106.780879,
                        106.782949, 106.782802, 106.782528, 106.783579, 106.783124,
                        106.780772, 106.780462, 106.780233, 106.780000, 106.780032, 106.779607,
                        106.781610, 106.781359, 106.781139, 106.782133, 106.781815,
                        106.779313, 106.780883, 106.779819, 106.779581
                };

                // Lưu thông tin tọa độ vào Map
                for (int i = 0; i < buildingNames.length; i++) {
                    buildingCoordinates.put(buildingNames[i], new double[]{buildingLatitudes[i], buildingLongitudes[i]});
                }

                int hubIndex = 1;
                for (Map.Entry<String, String[]> entry : hubBuildingMap.entrySet()) {
                    String hubName = entry.getKey();
                    String[] assignedBuildings = entry.getValue();

                    // Random tọa độ cho Hub trong phạm vi hợp lý
                    double hubLat = 10.88 + (random.nextDouble() * 0.01); // Từ 10.88 đến 10.89
                    double hubLon = 106.78 + (random.nextDouble() * 0.01); // Từ 106.78 đến 106.79

                    Hub hub = Hub.builder()
                            .address("Hub Address " + hubIndex)
                            .name(hubName)
                            .longitude(hubLon)
                            .latitude(hubLat)
                            .description("Hub Description " + hubIndex)
                            .build();
                    Hub savedHub = hubRepository.save(hub);

                    // Gán các building vào Hub
                    for (String buildingName : assignedBuildings) {
                        double[] coordinates = buildingCoordinates.get(buildingName);
                        if (coordinates != null) {
                            Building building = Building.builder()
                                    .latitude(coordinates[0])
                                    .longitude(coordinates[1])
                                    .name(buildingName)
                                    .description("Building Description for " + buildingName)
                                    .hub(savedHub)
                                    .build();
                            buildingRepository.save(building);
                        }
                    }
                    hubIndex++;
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

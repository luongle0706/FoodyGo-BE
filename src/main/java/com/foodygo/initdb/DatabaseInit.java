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
import java.util.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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
    private final OperatingHourRepository operatingHourRepository;

    public static final Map<Integer, String> WEEKDAYS = Map.of(
            1, "Thứ 2",
            2, "Thứ 3",
            3, "Thứ 4",
            4, "Thứ 5",
            5, "Thứ 6",
            6, "Thứ 7",
            7, "Chủ Nhật"
    );

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

            User user = null, admin, manager, seller = null;
            List<User> staff = new ArrayList<>();
            if (userRepository.count() == 0) {
                Role roleUser = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_USER);
                Role roleAdmin = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_ADMIN);
                Role roleStaff = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_STAFF);
                Role roleManager = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_MANAGER);
                Role roleSeller = roleRepository.getRoleByRoleName(EnumRoleNameType.ROLE_SELLER);

                user = User.builder()
                        .fullName("Lê Vũ Đức Lương")
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
                        .fullName("Nguyễn Thế Anh")
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
                        .fullName("Phạm Tấn Lộc")
                        .accessToken(null)
                        .refreshToken(null)
                        .email("manager@gmail.com")
                        .password(bCryptPasswordEncoder.encode("123456"))
                        .enabled(true)
                        .nonLocked(true)
                        .role(roleManager)
                        .build();
                userRepository.save(manager);

                for (int i = 1; i <= 5; i++) {
                    staff.add(userRepository.save(User.builder()
                            .fullName("Trịnh Trần Phương Tuấn")
                            .accessToken(null)
                            .refreshToken(null)
                            .email("staff" + i + "@gmail.com")
                            .password(bCryptPasswordEncoder.encode("123456"))
                            .enabled(true)
                            .nonLocked(true)
                            .role(roleStaff)
                            .build()));
                }

                seller = User.builder()
                        .fullName("Nguyễn Minh Quân")
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
                // Danh sách ánh xạ Hub → Buildings với tọa độ chính xác
                Map<String, Object[]> hubDataMap = new HashMap<>();
                hubDataMap.put("HUB A", new Object[]{10.883726, 106.779940, new String[]{"C3", "C4", "C5", "C6", "E1", "C2", "C1"}});
                hubDataMap.put("HUB B", new Object[]{10.882546, 106.781533, new String[]{"A5", "A3", "A2", "A4", "A1"}});
                hubDataMap.put("HUB C", new Object[]{10.883210, 106.782327, new String[]{"B3", "B5", "B2", "B1", "B4"}});
                hubDataMap.put("HUB D", new Object[]{10.884353, 106.781914, new String[]{"G1", "D6", "D4", "D3", "D2", "D5"}});
                hubDataMap.put("HUB E", new Object[]{10.884869, 106.780256, new String[]{"F1", "F2"}});

                // Tạo danh sách tọa độ building
                Map<String, double[]> buildingCoordinates = new HashMap<>();
                buildingCoordinates.put("A1", new double[]{10.881837, 106.781889});
                buildingCoordinates.put("A2", new double[]{10.882055, 106.781678});
                buildingCoordinates.put("A3", new double[]{10.882527, 106.781222});
                buildingCoordinates.put("A4", new double[]{10.881701, 106.781364});
                buildingCoordinates.put("A5", new double[]{10.882287, 106.780879});
                buildingCoordinates.put("B1", new double[]{10.882730, 106.782949});
                buildingCoordinates.put("B2", new double[]{10.883051, 106.782802});
                buildingCoordinates.put("B3", new double[]{10.883491, 106.782528});
                buildingCoordinates.put("B4", new double[]{10.883172, 106.783579});
                buildingCoordinates.put("B5", new double[]{10.883874, 106.783124});
                buildingCoordinates.put("C1", new double[]{10.883195, 106.780772});
                buildingCoordinates.put("C2", new double[]{10.883517, 106.780462});
                buildingCoordinates.put("C3", new double[]{10.883755, 106.780233});
                buildingCoordinates.put("C4", new double[]{10.884056, 106.780000});
                buildingCoordinates.put("C5", new double[]{10.883054, 106.780032});
                buildingCoordinates.put("C6", new double[]{10.883545, 106.779607});
                buildingCoordinates.put("D2", new double[]{10.884469, 106.781610});
                buildingCoordinates.put("D3", new double[]{10.884741, 106.781359});
                buildingCoordinates.put("D4", new double[]{10.884957, 106.781139});
                buildingCoordinates.put("D5", new double[]{10.884909, 106.782133});
                buildingCoordinates.put("D6", new double[]{10.885382, 106.781815});
                buildingCoordinates.put("E1", new double[]{10.884249, 106.779313});
                buildingCoordinates.put("F1", new double[]{10.885450, 106.779819});
                buildingCoordinates.put("F2", new double[]{10.885655, 106.779581});
                buildingCoordinates.put("G1", new double[]{10.885634, 106.780883});

                int hubIndex = 1;
                for (Map.Entry<String, Object[]> entry : hubDataMap.entrySet()) {
                    String hubName = entry.getKey();
                    double hubLat = (double) entry.getValue()[0];
                    double hubLon = (double) entry.getValue()[1];
                    String[] assignedBuildings = (String[]) entry.getValue()[2];

                    Hub hub = Hub.builder()
                            .address("Hub " + hubName + ", Đông Hoà, Dĩ An, Bình Dương, Vietnam")
                            .name(hubName)
                            .longitude(hubLon)
                            .latitude(hubLat)
                            .description("Hub " + hubName + " của ký túc xá khu B")
                            .build();
                    Hub savedHub = hubRepository.save(hub);

                    staff.get(hubIndex - 1).setHub(savedHub);
                    userRepository.save(staff.get(hubIndex - 1));

                    // Gán các building vào Hub
                    for (String buildingName : assignedBuildings) {
                        double[] coordinates = buildingCoordinates.get(buildingName);
                        if (coordinates != null) {
                            Building building = Building.builder()
                                    .latitude(coordinates[0])
                                    .longitude(coordinates[1])
                                    .name(buildingName)
                                    .description("Toà " + buildingName + " của ký túc xá khu B")
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
                restaurant = Restaurant.builder()
                        .name("Cơm tấm Ngô Quyền")
                        .phone("+84" + (100000000 + random.nextInt(900000000)))
                        .email("restaurant@foodygo.com")
                        .owner(seller)
                        .address((1 + random.nextInt(100)) + "/" + (1 + random.nextInt(100)) + " Street " + (1 + random.nextInt(100)))
                        .image("https://img-global.cpcdn.com/recipes/49876fe80303b991/640x640sq70/photo.webp")
                        .build();

                restaurant = restaurantRepository.save(restaurant);

                for (int i = 1; i < 8; i++) {
                    OperatingHour operatingHour = OperatingHour.builder()
                            .day(WEEKDAYS.get(i))
                            .isOpen(false)
                            .is24Hours(false)
                            .openingTime(LocalTime.of(7, 0))
                            .closingTime(LocalTime.of(23, 0))
                            .restaurant(restaurant)
                            .build();
                    operatingHourRepository.save(operatingHour);
                }

                Wallet sellerWallet = Wallet.builder()
                        .balance(0.0)
                        .walletType(WalletType.RESTAURANT)
                        .restaurant(restaurant)
                        .build();
                walletRepository.save(sellerWallet);

                for (int j = 0; j < 10; j++) {
                    String[] categoryNames = {"Cơm tấm", "Bún thịt nướng", "Gà rán", "Hủ tiếu", "Bánh mì", "Món chay", "Hải sản", "Lẩu", "Nước uống", "Tráng miệng"};
                    String categoryName = categoryNames[j % categoryNames.length];
                    Category category = Category.builder()
                            .name(categoryName)
                            .description("Chuyên mục " + categoryName + " tại " + restaurant.getName())
                            .restaurant(restaurant)
                            .build();
                    categoryRepository.save(category);

                    String[] productNames = {
                            "Cơm sườn bì chả", "Bún thịt nướng", "Gà rán sốt mật ong", "Hủ tiếu Nam Vang", "Bánh mì thịt nướng",
                            "Đậu hủ chiên giòn", "Tôm nướng muối ớt", "Lẩu thái hải sản", "Trà tắc", "Chè đậu xanh"
                    };

                    String[] productImages = {
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/com-tam-suon-bi-cha.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/bun-thit-nuong-kieu-mien-nam.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/ga-sot-mat-ong.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/hu-tieu-nam-vang-1.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/cach-lam-banh-mi-thit-nuong.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/cach-chien-dau-hu.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/cach-lam-tom-nuong-muoi-ot.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/lau-thai-hai-san-gofood-anh-2.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/cach-lam-tra-tac.jpg",
                            "https://foodygo-storage.s3.ap-southeast-1.amazonaws.com/productImage/che-dau-xanh-nuoc-cot-dua.jpeg"
                    };

                    int randomPrice = ThreadLocalRandom.current().nextInt(30, 120);

                    Product product = Product.builder()
                            .code("R" + restaurant.getId() + "P" + j)
                            .name(productNames[j % productNames.length])
                            .price(randomPrice * 1000.0)
                            .image(productImages[j % productImages.length])
                            .description("Món " + productNames[j % productNames.length] + " tại " + restaurant.getName())
                            .prepareTime(ThreadLocalRandom.current().nextDouble(10, 120))
                            .restaurant(restaurant)
                            .category(category)
                            .build();
                    productRepository.save(product);

                    List<Product> productList = new ArrayList<>();
                    productList.add(product);

                    String[] addonSectionNames = {"Chọn thêm topping", "Thêm nước sốt", "Thêm rau sống", "Chọn mức cay", "Thêm chả trứng"};
                    AddonSection addonSection = AddonSection.builder()
                            .name(addonSectionNames[j % addonSectionNames.length])
                            .maxChoice(((int) (Math.random() * 3)) + 1)
                            .products(productList)
                            .build();
                    addonSectionRepository.save(addonSection);

                    String[][] addonItems = {
                            {"Trứng ốp la", "Thịt chả", "Đậu hủ chiên"},
                            {"Sốt tiêu đen", "Sốt tỏi", "Sốt me"},
                            {"Xà lách", "Dưa leo", "Rau thơm"},
                            {"Ít cay", "Cay vừa", "Cay nhiều"},
                            {"Chả trứng nhỏ", "Chả trứng lớn", "Chả trứng đặc biệt"}
                    };

                    for (int k = 0; k < 3; k++) {
                        int randomAddonItemPrice = ThreadLocalRandom.current().nextInt(5, 20);
                        AddonItem addonItem = AddonItem.builder()
                                .name(addonItems[j % addonItems.length][k])
                                .price(randomAddonItemPrice * 1000.0)
                                .section(addonSection)
                                .quantity(ThreadLocalRandom.current().nextInt(1, 10))
                                .build();
                        addonItemRepository.save(addonItem);
                    }
                }
            }

//            if (orderRepository.count() == 0) {
//                List<Hub> hubs = hubRepository.findAll();
//
//                // Phí dịch vụ & vận chuyển
//                double shippingFee = random.nextDouble() * 5 + 2;
//                double serviceFee = random.nextDouble() * 3 + 1;
//                double totalPrice = 0;
//
//                // Tạo đơn hàng
//                for (Hub hub : hubs) {
//                    Order order = Order.builder()
//                            .time(LocalDateTime.now().minusDays(random.nextInt(10)))
//                            .shippingFee(shippingFee)
//                            .serviceFee(serviceFee)
//                            .totalPrice(totalPrice + shippingFee + serviceFee)
//                            .status(OrderStatus.RESTAURANT_ACCEPTED)
//                            .expectedDeliveryTime(LocalDateTime.now().plusHours(random.nextInt(5) + 1))
//                            .customerPhone(user != null ? user.getPhone() : "190238019283")
//                            .shipperPhone("+84" + (100000000 + random.nextInt(900000000)))
//                            .notes("Giao hàng trước 6h tối")
//                            .employee(user)
//                            .customer(customer)
//                            .restaurant(restaurant)
//                            .hub(hub)
//                            .build();
//                    orderRepository.save(order);
//                }
//            }
//
//            if (orderDetailRepository.count() == 0) {
//                List<Order> orders = orderRepository.findAll();
//                Product product1 = productRepository.findById(1).orElseThrow();
//                Product product2 = productRepository.findById(2).orElseThrow();
//
//                for (Order order : orders) {
//                    OrderDetail orderDetail1 = OrderDetail.builder()
//                            .order(order)
//                            .product(product1)
//                            .price(product1.getPrice())
//                            .quantity(1)
//                            .build();
//                    OrderDetail orderDetail2 = OrderDetail.builder()
//                            .order(order)
//                            .product(product2)
//                            .price(product2.getPrice())
//                            .quantity(1)
//                            .build();
//                    orderDetailRepository.save(orderDetail1);
//                    orderDetailRepository.save(orderDetail2);
//                }
//            }
        };
    }
}

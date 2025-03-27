package com.foodygo.service.impl;

import com.foodygo.dto.response.DashboardResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.Restaurant;
import com.foodygo.enums.OrderStatus;
import com.foodygo.repository.OrderRepository;
import com.foodygo.service.spec.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private static final int FOODYXU_TO_VND_RATE = 1000;

    @Override
    public DashboardResponse getDashboardData() {
        List<Order> allOrders = orderRepository.findAll();

        return new DashboardResponse(
                getRevenueStatistics(allOrders),
                getOrderStatusStatistics(allOrders),
                getTopRestaurants(allOrders)
        );
    }

    private DashboardResponse.RevenueStatistics getRevenueStatistics(List<Order> allOrders) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfYear = today.withDayOfYear(1);

        // Filter orders for different time periods
        List<Order> todayOrders = allOrders.stream()
                .filter(order -> order.getTime().toLocalDate().equals(today))
                .toList();

        List<Order> thisWeekOrders = allOrders.stream()
                .filter(order -> !order.getTime().toLocalDate().isBefore(startOfWeek))
                .toList();

        List<Order> thisMonthOrders = allOrders.stream()
                .filter(order -> !order.getTime().toLocalDate().isBefore(startOfMonth))
                .toList();

        List<Order> thisYearOrders = allOrders.stream()
                .filter(order -> !order.getTime().toLocalDate().isBefore(startOfYear))
                .toList();

        // Calculate totals
        int todayOrderCount = todayOrders.size();
        int thisWeekOrderCount = thisWeekOrders.size();
        int thisMonthOrderCount = thisMonthOrders.size();
        int thisYearOrderCount = thisYearOrders.size();

        double todayRevenue = convertToVND(todayOrders.stream().mapToDouble(Order::getTotalPrice).sum());
        double thisWeekRevenue = convertToVND(thisWeekOrders.stream().mapToDouble(Order::getTotalPrice).sum());
        double thisMonthRevenue = convertToVND(thisMonthOrders.stream().mapToDouble(Order::getTotalPrice).sum());
        double thisYearRevenue = convertToVND(thisYearOrders.stream().mapToDouble(Order::getTotalPrice).sum());

        // Get last 12 months revenue
        List<DashboardResponse.RevenueStatistics.MonthlyRevenue> last12MonthsRevenue = getLast12MonthsRevenue(allOrders);

        return new DashboardResponse.RevenueStatistics(
                todayOrderCount,
                thisWeekOrderCount,
                thisMonthOrderCount,
                thisYearOrderCount,
                todayRevenue,
                thisWeekRevenue,
                thisMonthRevenue,
                thisYearRevenue,
                last12MonthsRevenue
        );
    }

    private List<DashboardResponse.RevenueStatistics.MonthlyRevenue> getLast12MonthsRevenue(List<Order> allOrders) {
        YearMonth currentMonth = YearMonth.now();
        Map<YearMonth, Double> monthlyRevenueMap = new HashMap<>();

        // Initialize last 12 months with zero revenue
        for (int i = 11; i >= 0; i--) {
            monthlyRevenueMap.put(currentMonth.minusMonths(i), 0.0);
        }

        // Calculate revenue for each month
        for (Order order : allOrders) {
            YearMonth orderMonth = YearMonth.from(order.getTime());
            if (orderMonth.isAfter(currentMonth.minusMonths(12)) && !orderMonth.isAfter(currentMonth)) {
                monthlyRevenueMap.put(orderMonth, monthlyRevenueMap.getOrDefault(orderMonth, 0.0) + order.getTotalPrice());
            }
        }

        // Format the result
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        List<DashboardResponse.RevenueStatistics.MonthlyRevenue> result = new ArrayList<>();

        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            String formattedMonth = month.format(formatter);
            double revenue = convertToVND(monthlyRevenueMap.getOrDefault(month, 0.0));
            result.add(new DashboardResponse.RevenueStatistics.MonthlyRevenue(formattedMonth, revenue));
        }

        return result;
    }

    private DashboardResponse.OrderStatusStatistics getOrderStatusStatistics(List<Order> allOrders) {
        int totalOrders = allOrders.size();
        if (totalOrders == 0) {
            return new DashboardResponse.OrderStatusStatistics(0, Collections.emptyList());
        }

        // Count orders by status
        Map<OrderStatus, Long> statusCounts = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

        // Convert to DTO
        List<DashboardResponse.OrderStatusStatistics.StatusData> statusDistribution = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            long count = statusCounts.getOrDefault(status, 0L);
            double percentage = (count * 100.0) / totalOrders;

            statusDistribution.add(new DashboardResponse.OrderStatusStatistics.StatusData(
                    status.name(),
                    (int) count,
                    Math.round(percentage * 10) / 10.0  // Round to 1 decimal place
            ));
        }

        return new DashboardResponse.OrderStatusStatistics(totalOrders, statusDistribution);
    }

    private DashboardResponse.TopRestaurants getTopRestaurants(List<Order> allOrders) {
        // Group orders by restaurant
        Map<Restaurant, List<Order>> ordersByRestaurant = allOrders.stream()
                .filter(order -> order.getRestaurant() != null)
                .collect(Collectors.groupingBy(Order::getRestaurant));

        // Calculate revenue and order count for each restaurant
        List<DashboardResponse.TopRestaurants.RestaurantData> restaurantDataList = new ArrayList<>();

        for (Map.Entry<Restaurant, List<Order>> entry : ordersByRestaurant.entrySet()) {
            Restaurant restaurant = entry.getKey();
            List<Order> restaurantOrders = entry.getValue();

            double totalRevenue = convertToVND(restaurantOrders.stream()
                    .mapToDouble(Order::getTotalPrice)
                    .sum());

            restaurantDataList.add(new DashboardResponse.TopRestaurants.RestaurantData(
                    restaurant.getId().longValue(),
                    restaurant.getName(),
                    totalRevenue,
                    restaurantOrders.size()
            ));
        }

        // Sort by revenue (descending) and take top 10
        List<DashboardResponse.TopRestaurants.RestaurantData> topRestaurants = restaurantDataList.stream()
                .sorted(Comparator.comparingDouble(DashboardResponse.TopRestaurants.RestaurantData::totalRevenue).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return new DashboardResponse.TopRestaurants(topRestaurants);
    }

    private double convertToVND(double foodyXuAmount) {
        return foodyXuAmount * FOODYXU_TO_VND_RATE;
    }
}
package com.foodygo.dto.response;

import java.util.List;

public record DashboardResponse(

        RevenueStatistics revenueStatistics,

        OrderStatusStatistics orderStatusStatistics,

        TopRestaurants topRestaurants

) {

    public record RevenueStatistics(
            int todayOrders,
            int thisWeekOrders,
            int thisMonthOrders,
            int thisYearOrders,
            double todayRevenue,
            double thisWeekRevenue,
            double thisMonthRevenue,
            double thisYearRevenue,
            List<MonthlyRevenue> last12MonthsRevenue
    ) {
        public record MonthlyRevenue(
                String month,
                double revenue
        ) {}
    }

    public record OrderStatusStatistics(
            int totalOrders,
            List<StatusData> statusDistribution
    ) {
        public record StatusData(
                String status,
                int count,
                double percentage
        ) {}
    }

    public record TopRestaurants(
            List<RestaurantData> restaurants
    ) {
        public record RestaurantData(
                Long id,
                String name,
                double totalRevenue,
                int totalOrders
        ) {}
    }
}
package com.foodygo.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DashboardResponse(
        CategoryStatistics categoryStatistics,

        RevenueStatistics revenueStatistics,

        OrderStatusStatistics orderStatusStatistics,

        OrderDeliveryStatistics orderDeliveryStatistics
) {
    public record CategoryStatistics(
            int totalOrders,
            List<CategoryData> categoryDistribution
    ) {
        public record CategoryData(
                String categoryName,
                int orderCount,
                double percentage
        ) {}
    }

    public record RevenueStatistics(
            double totalRevenue,
            double currentMonthRevenue,
            double currentYearRevenue,
            List<MonthlyRevenue> monthlyRevenue,
            List<DailyRevenue> dailyRevenue
    ) {
        public record MonthlyRevenue(
                String month, // Format: "YYYY-MM"
                double revenue
        ) {}

        public record DailyRevenue(
                LocalDate date,
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

    public record OrderDeliveryStatistics(
            int totalDeliveredOrders,
            int onTimeOrders,
            int delayedOrders,
            double onTimePercentage,
            double delayedPercentage,
            double averageDelayTime
    ) {}
}
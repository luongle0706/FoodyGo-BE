package com.foodygo.dto.paging;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderDetail;
import com.foodygo.enums.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("dynamicFilter")
public class OrderPagingResponse {
    Integer id;
    Double shippingFee;
    Double serviceFee;
    Double totalPrice;
    Integer totalItems;
    OrderStatus status;
    LocalDateTime expectedDeliveryTime;
    LocalDateTime time;
    LocalDateTime confirmedAt;
    String customerPhone;
    String shipperPhone;
    String notes;
    Integer employeeId;
    String employeeName;
    String customerName;
    Integer restaurantId;
    String restaurantName;
    Integer hubId;
    String hubName;
    List<OrderDetailResponse> orderDetails;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonFilter("dynamicFilter")
    public static class OrderDetailResponse {
        Integer id;
        Integer orderId;
        Integer quantity;
        Double price;
        String addonItems;
        String productName;
    }

    public static OrderPagingResponse fromEntity(Order order) {
        return OrderPagingResponse.builder()
                .id(order.getId())
                .shippingFee(order.getShippingFee())
                .serviceFee(order.getServiceFee())
                .totalPrice(order.getTotalPrice())
                .totalItems(order.getOrderDetails().stream().mapToInt(OrderDetail::getQuantity).sum())
                .status(order.getStatus())
                .expectedDeliveryTime(order.getExpectedDeliveryTime())
                .time(order.getTime())
                .confirmedAt(order.getConfirmedAt())
                .customerPhone(order.getCustomerPhone())
                .shipperPhone(order.getShipperPhone())
                .notes(order.getNotes())
                .employeeId(order.getEmployee().getUserID())
                .employeeName(order.getEmployee().getFullName())
                .customerName(order.getCustomer().getUser().getFullName())
                .restaurantId(order.getRestaurant().getId())
                .restaurantName(order.getRestaurant().getName())
                .hubId(order.getHub().getId())
                .hubName(order.getHub().getName())
                .orderDetails(order.getOrderDetails().stream().map(detail ->
                        OrderDetailResponse.builder()
                                .id(detail.getId())
                                .orderId(order.getId())
                                .quantity(detail.getQuantity())
                                .price(detail.getPrice())
                                .addonItems(detail.getAddonItems())
                                .productName(detail.getProduct().getName())
                                .build()
                ).toList())
                .build();
    }

    public static Specification<Order> filterByFields(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            params.forEach((key, value) -> {
                switch (key) {
                    case "id":
                        predicates.add(criteriaBuilder.equal(root.get("id"), Integer.parseInt(value)));
                        break;
                    case "status":
                        predicates.add(criteriaBuilder.equal(root.get("status"), OrderStatus.valueOf(value)));
                        break;
                    case "customerPhone":
                        predicates.add(criteriaBuilder.like(root.get("customer").get("user").get("phone"), "%" + value + "%"));
                        break;
                    case "shipperPhone":
                        predicates.add(criteriaBuilder.like(root.get("shipperPhone"), "%" + value + "%"));
                        break;
                    case "restaurantId":
                        predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), Integer.parseInt(value)));
                        break;
                    case "restaurantName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("restaurant").get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "hubId":
                        predicates.add(criteriaBuilder.equal(root.get("hub").get("id"), Integer.parseInt(value)));
                        break;
                    case "hubName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("hub").get("name")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "employeeId":
                        predicates.add(criteriaBuilder.equal(root.get("employee").get("userID"), Integer.parseInt(value)));
                        break;
                    case "employeeName":
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("employee").get("fullName")), "%" + value.toLowerCase() + "%"));
                        break;
                    case "totalPriceMin":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalPrice"), Double.parseDouble(value)));
                        break;
                    case "totalPriceMax":
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalPrice"), Double.parseDouble(value)));
                        break;
                    case "expectedDeliveryTimeFrom":
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expectedDeliveryTime"), LocalDateTime.parse(value)));
                        break;
                    case "expectedDeliveryTimeTo":
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expectedDeliveryTime"), LocalDateTime.parse(value)));
                        break;
                }
            });

            predicates.add(criteriaBuilder.equal(root.get("deleted"), false)); // Exclude deleted orders
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

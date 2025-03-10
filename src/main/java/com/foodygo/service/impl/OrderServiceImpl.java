package com.foodygo.service.impl;

import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.paging.OrderPagingResponse;
import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderDetailCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.*;
import com.foodygo.enums.OrderStatus;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OrderDetailMapper;
import com.foodygo.mapper.OrderMapper;
import com.foodygo.repository.OrderDetailRepository;
import com.foodygo.repository.OrderRepository;
import com.foodygo.service.spec.*;
import com.foodygo.utils.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final TransactionService transactionService;
    private final OrderActivityService orderActivityService;
    private final HubService hubService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WalletService walletService;

    @Value("${business.service-fee}")
    private double serviceFeePercentage;

    @Override
    @Transactional
    public int createOrder(OrderCreateRequest request) {
        double serviceFee = serviceFeePercentage * (request.getProductPrice() + request.getShippingFee());
        Customer customer = customerService.findById(request.getCustomerId());
        Restaurant restaurant = restaurantService.getRestaurantById(request.getRestaurantId());
        Hub hub = hubService.getHubById(request.getHubId());
        Order order = Order.builder()
                .id(null)
                .time(request.getTime())
                .shippingFee(request.getShippingFee())
                .serviceFee(serviceFee)
                .totalPrice(request.getProductPrice() + request.getShippingFee() + serviceFee)
                .status(OrderStatus.CANCELLED)
                .expectedDeliveryTime(request.getExpectedDeliveryTime())
                .customerPhone(request.getCustomerPhone())
                .shipperPhone(null)
                .employee(null)
                .customer(customer)
                .restaurant(restaurant)
                .hub(hub)
                .notes(request.getNotes())
                .build();
        orderRepository.save(order);

        for (OrderDetailCreateRequest odcr : request.getOrderDetails()) {
            Product p = productService.getProductById(odcr.getProductId());
            OrderDetail od = OrderDetail.builder()
                    .quantity(odcr.getQuantity())
                    .price(odcr.getPrice())
                    .addonItems(odcr.getAddonItems())
                    .order(order)
                    .product(p)
                    .build();
            orderDetailRepository.save(od);
        }

        walletService.paymentOrder(order);
        order.setStatus(OrderStatus.ORDERED);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Integer orderId, OrderUpdateRequest orderUpdateRequest) {
        Order order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();
        OrderMapper.INSTANCE.updateOrderFromDto(orderUpdateRequest, order);
        order.setConfirmedAt(LocalDateTime.now());
        orderRepository.save(order);

        String imageUrl = "null";
//        if (orderUpdateRequest.getImage() != null && !orderUpdateRequest.getImage().isEmpty()) {
//            try {
//                imageUrl = firebaseStorageService.uploadFile(orderUpdateRequest.getImage());
//            } catch (IOException e) {
//                throw new RuntimeException("Update image failed", e);
//            }
//        }

        //create order activity
        orderActivityService.logOrderStatusChange(orderId, orderUpdateRequest.getUserId(), oldStatus,
                orderUpdateRequest.getStatus(), imageUrl);

        List<OrderDetail> orderDetails = order.getOrderDetails();
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(OrderDetailMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderMapper.INSTANCE.toDto(order);
        orderResponse.setOrderDetails(orderDetailResponses);

        return orderResponse;
    }

//    private void updateOrderDetails(Order order, List<OrderDetailUpdateRequest> orderDetailUpdateRequests) {
//        Map<Integer, OrderDetail> orderDetailMap = order.getOrderDetails().stream()
//                .collect(Collectors.toMap(OrderDetail::getId, orderDetail -> orderDetail));
//        List<OrderDetail> updatedOrderDetails = new ArrayList<>();
//

    /// /        List<Integer> productIds = orderDetailUpdateRequests.stream()
    /// /                .map(OrderDetailUpdateRequest::getProductId)
    /// /                .toList();
    /// /
    /// /        Map<Integer, Product> productMap = productService.getProductsByIds(productIds)
    /// /                .stream()
    /// /                .collect(Collectors.toMap(Product::getId, p -> p));
//
//        for(OrderDetailUpdateRequest orderDetailUpdateRequest : orderDetailUpdateRequests) {
//            if(orderDetailUpdateRequest.getId() == null) {
//                OrderDetail newOrderDetail = OrderDetailMapper.INSTANCE.toEntity(orderDetailUpdateRequest);
//                newOrderDetail.setProduct(productService.getProductById(orderDetailUpdateRequest.getProductId()));
//                updatedOrderDetails.add(newOrderDetail);
//            } else {
//                OrderDetail oldOrderDetail = orderDetailMap.get(orderDetailUpdateRequest.getId());
//                if(oldOrderDetail != null) {
//                    OrderDetailMapper.INSTANCE.updateOrderDetailFromDto(orderDetailUpdateRequest, oldOrderDetail);
//                    updatedOrderDetails.add(oldOrderDetail);
//                    orderDetailMap.remove(orderDetailUpdateRequest.getId());
//                }
//            }
//        }
//        orderDetailRepository.deleteAll(orderDetailMap.values());
//        orderDetailRepository.saveAll(updatedOrderDetails);
//        order.setOrderDetails(updatedOrderDetails);
//    }
    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Order not found"));
    }

    @Override
    public OrderResponse getOrderResponseById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Order not found"));
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                .map(OrderDetailMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderMapper.INSTANCE.toDto(order);
        orderResponse.setOrderDetails(orderDetailResponses);
        return orderResponse;
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id) {
        Order order = getOrderById(id);
        orderDetailService.deleteOrderDetailsByOrderId(id);
        orderActivityService.deleteOrderActivitiesByOrderId(id);
        transactionService.deleteTransactionsByOrderId(id);
        orderRepository.delete(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return getOrderResponses(orders);
    }

    @Override
    public Page<OrderResponse> getAllOrdersByEmployeeId(Integer employeeId, Pageable pageable) {
        Page<Order> orders = orderRepository.findOrdersByEmployeeId(employeeId, pageable);
        return getOrderResponses(orders);
    }

    private Page<OrderResponse> getOrderResponses(Page<Order> orders) {
        return orders.map(order -> {
            OrderResponse orderResponse = OrderMapper.INSTANCE.toDto(order);
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                    .map(OrderDetailMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
            int totalItems = orderDetailResponses.stream()
                    .mapToInt(OrderDetailResponse::getQuantity)
                    .sum();
            orderResponse.setTotalItems(totalItems);
            orderResponse.setOrderDetails(orderDetailResponses);
            return orderResponse;
        });
    }

    @Override
    public Page<OrderResponse> getAllOrdersByCustomerId(Integer customerId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByCustomerId(customerId, pageable);
        return getOrderResponses(orders);
    }

    @Override
    public Page<OrderResponse> getAllOrdersByRestaurantId(Integer restaurantId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByRestaurantId(restaurantId, pageable);
        return getOrderResponses(orders);
    }

    @Override
    public Page<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return getOrderResponses(orders);
    }

    @Override
    public MappingJacksonValue getOrders(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<Order> orderSpecification = OrderPagingResponse.filterByFields(request.getFilters());
        Page<Order> page = orderRepository.findAll(orderSpecification, pageable);
        List<OrderPagingResponse> mappedDTOs = page.getContent().stream().map(OrderPagingResponse::fromEntity).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, page, mappedDTOs, "Get orders");
    }


}

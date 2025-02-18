package com.foodygo.service;

import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderDetailCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderDetail;
import com.foodygo.entity.Product;
import com.foodygo.enums.OrderStatus;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OrderDetailMapper;
import com.foodygo.mapper.OrderMapper;
import com.foodygo.repository.OrderDetailRepository;
import com.foodygo.repository.OrderRepository;
import com.foodygo.utils.QuanTest_FirebaseStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final TransactionService transactionService;
    private final OrderActivityService orderActivityService;
    private final QuanTest_FirebaseStorageService firebaseStorageService;
    private final HubService hubService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        Order order = OrderMapper.INSTANCE.toEntity(orderCreateRequest);
        order.setEmployee(userService.findById(orderCreateRequest.getEmployeeId()));
        order.setCustomer(customerService.findById(orderCreateRequest.getCustomerId()));
        order.setRestaurant(restaurantService.getRestaurantById(orderCreateRequest.getRestaurantId()));
        order.setHub(hubService.getHubById(orderCreateRequest.getHubId()));
        order.setStatus(OrderStatus.ORDERED);
        orderRepository.save(order);

        List<Integer> productIds = orderCreateRequest.getOrderDetails().stream()
                .map(OrderDetailCreateRequest::getProductId)
                .toList();

        Map<Integer, Product> productMap = productService.getProductsByIds(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<OrderDetail> orderDetails = orderCreateRequest.getOrderDetails().stream()
                .map(dto -> OrderDetail.builder()
                        .quantity(dto.getQuantity())
                        .price(dto.getPrice())
                        .addonItems(dto.getAddonItems())
                        .order(order)
                        .product(productMap.get(dto.getProductId()))
                        .build()).toList();

        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);

        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(OrderDetailMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

        OrderResponse orderResponse = OrderMapper.INSTANCE.toDto(order);
        orderResponse.setOrderDetails(orderDetailResponses);

        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Integer orderId, OrderUpdateRequest orderUpdateRequest) {
        Order order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();
        OrderMapper.INSTANCE.updateOrderFromDto(orderUpdateRequest, order);
//        if(orderUpdateRequest.getOrderDetailUpdateRequests() != null) {
//            updateOrderDetails(order, orderUpdateRequest.getOrderDetailUpdateRequests());
//        }
        orderRepository.save(order);

        String imageUrl = null;
        if (orderUpdateRequest.getImage() != null && !orderUpdateRequest.getImage().isEmpty()) {
            try {
                imageUrl = firebaseStorageService.uploadFile(orderUpdateRequest.getImage());
            } catch (IOException e) {
                throw new RuntimeException("Update image failed", e);
            }
        }

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

    private Integer getUserIdFromRequest(OrderUpdateRequest orderUpdateRequest) {
        if (orderUpdateRequest.getUserId() != null) {
            return orderUpdateRequest.getUserId();
        } else if (orderUpdateRequest.getEmployeeId() != null) {
            return orderUpdateRequest.getEmployeeId();
        } else {
            throw new IdNotFoundException("Not any user found");
        }
    }

//    private void updateOrderDetails(Order order, List<OrderDetailUpdateRequest> orderDetailUpdateRequests) {
//        Map<Integer, OrderDetail> orderDetailMap = order.getOrderDetails().stream()
//                .collect(Collectors.toMap(OrderDetail::getId, orderDetail -> orderDetail));
//        List<OrderDetail> updatedOrderDetails = new ArrayList<>();
//
////        List<Integer> productIds = orderDetailUpdateRequests.stream()
////                .map(OrderDetailUpdateRequest::getProductId)
////                .toList();
////
////        Map<Integer, Product> productMap = productService.getProductsByIds(productIds)
////                .stream()
////                .collect(Collectors.toMap(Product::getId, p -> p));
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
}

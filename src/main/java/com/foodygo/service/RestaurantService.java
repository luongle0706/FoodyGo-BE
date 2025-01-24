package com.foodygo.service;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantService {
    Restaurant getRestaurantById(Integer restaurantId);
    RestaurantDTO getRestaurantDTOById(Integer restaurantId);

    List<Restaurant> getAllRestaurants();
    Page<RestaurantDTO> getAllRestaurantDTOs(Pageable pageable);

    List<Restaurant> searchRestaurantsByName(String name);
    Page<RestaurantDTO> searchRestaurantsByName(String name, Pageable pageable);

    void updateRestaurantInfo(RestaurantDTO restaurantDTO);

    void createRestaurant(RestaurantDTO restaurantDTO);

    void deleteRestaurant(Integer restaurantId);

    boolean switchRestaurantAvailability(Integer restaurantId);
}

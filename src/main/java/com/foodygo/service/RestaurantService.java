package com.foodygo.service;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant getRestaurantById(Integer restaurantId);
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantByProductId(Integer productId);
    void updateRestaurantInfo(RestaurantDTO restaurantDTO);
    void createRestaurant(RestaurantDTO restaurantDTO);
    void deleteRestaurant(Integer restaurantId);
}

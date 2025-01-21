package com.foodygo.service;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Override
    public Restaurant getRestaurantById(Integer restaurantId) {
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurantsPagination(Integer page, Integer items) {
        return null;
    }

    @Override
    public Restaurant getRestaurantByProductId(Integer productId) {
        return null;
    }

    @Override
    public void updateRestaurantInfo(RestaurantDTO restaurantDTO) {

    }

    @Override
    public void createRestaurant(RestaurantDTO restaurantDTO) {

    }

    @Override
    public void deleteRestaurant(Integer restaurantId) {

    }

    @Override
    public boolean switchRestaurantAvailability(Integer restaurantId) {
        return false;
    }
}

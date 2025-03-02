package com.foodygo.service.spec;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.response.RestaurantResponseDTO;
import com.foodygo.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

public interface RestaurantService {
    Restaurant getRestaurantById(Integer restaurantId);

    RestaurantResponseDTO getRestaurantDTOById(Integer restaurantId);

    List<Restaurant> getAllRestaurants();

    Page<RestaurantResponseDTO> getAllRestaurantDTOs(Pageable pageable);

    MappingJacksonValue getAllRestaurantDTOs(PagingRequest pagingRequest);

    List<Restaurant> searchRestaurantsByName(String name);

    Page<RestaurantResponseDTO> searchRestaurantsByName(String name, Pageable pageable);

    void updateRestaurantInfo(RestaurantDTO restaurantDTO);

    void createRestaurant(RestaurantDTO restaurantDTO);

    void deleteRestaurant(Integer restaurantId);

    boolean switchRestaurantAvailability(Integer restaurantId);
}

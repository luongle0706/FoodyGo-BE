package com.foodygo.service;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.RestaurantMapper;
import com.foodygo.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant getRestaurantById(Integer restaurantId) {
        return restaurantRepository.findByIdAndDeletedFalse(restaurantId)
                .orElseThrow(() -> new ElementNotFoundException("Restaurant not found with id " + restaurantId));
    }

    @Override
    public RestaurantDTO getRestaurantDTOById(Integer restaurantId) {
        return RestaurantMapper.INSTANCE.toDTO(getRestaurantById(restaurantId));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByDeletedFalse();
    }

    @Override
    public Page<RestaurantDTO> getAllRestaurantDTOsPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findByDeletedFalse(pageable).map(RestaurantMapper.INSTANCE::toDTO);
    }

    @Override
    public void updateRestaurantInfo(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = getRestaurantById(restaurantDTO.id());
        restaurant.setName(restaurantDTO.name());
        restaurant.setPhone(restaurantDTO.phone());
        restaurant.setEmail(restaurantDTO.email());
        restaurant.setAddress(restaurantDTO.address());
        restaurant.setImage(restaurant.getImage());
        restaurantRepository.save(restaurant);
    }

    @Override
    public void createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.name())
                .phone(restaurantDTO.phone())
                .email(restaurantDTO.email())
                .address(restaurantDTO.address())
                .image(restaurantDTO.image())
                .build();
        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Integer restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.setDeleted(true);
        restaurantRepository.save(restaurant);
    }

    @Override
    public boolean switchRestaurantAvailability(Integer restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        if (restaurant.isAvailable()) {
            restaurant.setAvailable(false);
        } else {
            restaurant.setAvailable(true);
        }
        restaurantRepository.save(restaurant);
        return restaurant.isAvailable();
    }
}

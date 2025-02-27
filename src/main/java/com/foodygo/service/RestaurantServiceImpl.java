package com.foodygo.service;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.entity.Restaurant;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.RestaurantMapper;
import com.foodygo.repository.RestaurantRepository;
import com.foodygo.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    public Page<RestaurantDTO> getAllRestaurantDTOs(Pageable pageable) {
        return restaurantRepository.findByDeletedFalse(pageable).map(RestaurantMapper.INSTANCE::toDTO);
    }

    @Override
    public MappingJacksonValue getAllRestaurantDTOs(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<Restaurant> specification = RestaurantDTO.filterByFields(request.getFilters());
        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<RestaurantDTO> mappedResposne = restaurants.getContent().stream().map(RestaurantMapper.INSTANCE::toDTO).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, restaurants, mappedResposne);
    }

    @Override
    public List<Restaurant> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<RestaurantDTO> searchRestaurantsByName(String name, Pageable pageable) {
        return restaurantRepository.findByNameContainingIgnoreCase(name, pageable).map(RestaurantMapper.INSTANCE::toDTO);
    }

    @Override
    public void updateRestaurantInfo(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = getRestaurantById(restaurantDTO.getId());
        restaurant.setName(restaurantDTO.getName());
        restaurant.setPhone(restaurantDTO.getPhone());
        restaurant.setEmail(restaurantDTO.getEmail());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setImage(restaurant.getImage());
        restaurantRepository.save(restaurant);
    }

    @Override
    public void createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.getName())
                .phone(restaurantDTO.getPhone())
                .email(restaurantDTO.getEmail())
                .address(restaurantDTO.getAddress())
                .image(restaurantDTO.getImage())
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

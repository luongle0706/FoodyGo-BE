package com.foodygo.service.impl;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.request.RestaurantCreateRequest;
import com.foodygo.dto.response.RestaurantResponseDTO;
import com.foodygo.entity.OperatingHour;
import com.foodygo.entity.Restaurant;
import com.foodygo.entity.User;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.RestaurantMapper;
import com.foodygo.mapper.RestaurantResponseMapper;
import com.foodygo.repository.OperatingHourRepository;
import com.foodygo.repository.RestaurantRepository;
import com.foodygo.service.spec.OperatingHourService;
import com.foodygo.service.spec.RestaurantService;
import com.foodygo.service.spec.UserService;
import com.foodygo.utils.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OperatingHourService operatingHourService;
    private final UserService userService;

    public static final Map<Integer, String> WEEKDAYS = Map.of(
            1, "Thứ 2",
            2, "Thứ 3",
            3, "Thứ 4",
            4, "Thứ 5",
            5, "Thứ 6",
            6, "Thứ 7",
            7, "Chủ Nhật"
    );

    @Override
    public Restaurant getRestaurantById(Integer restaurantId) {
        return restaurantRepository.findByIdAndDeletedFalse(restaurantId)
                .orElseThrow(() -> new ElementNotFoundException("Restaurant not found with id " + restaurantId));
    }

    @Override
    public RestaurantResponseDTO getRestaurantDTOById(Integer restaurantId) {
        return RestaurantResponseMapper.INSTANCE.toDTO(getRestaurantById(restaurantId));
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByDeletedFalse();
    }

    @Override
    public Page<RestaurantResponseDTO> getAllRestaurantDTOs(Pageable pageable) {
        return restaurantRepository.findByDeletedFalse(pageable).map(RestaurantResponseMapper.INSTANCE::toDTO);
    }

    @Override
    public MappingJacksonValue getAllRestaurantDTOs(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<Restaurant> specification = RestaurantDTO.filterByFields(request.getFilters());
        Page<Restaurant> restaurants = restaurantRepository.findAll(specification, pageable);
        List<RestaurantDTO> mappedResposne = restaurants.getContent().stream().map(RestaurantMapper.INSTANCE::toDTO).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, restaurants, mappedResposne, "Get all restaurants");
    }

    @Override
    public List<Restaurant> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<RestaurantResponseDTO> searchRestaurantsByName(String name, Pageable pageable) {
        return restaurantRepository.findByNameContainingIgnoreCase(name, pageable).map(RestaurantResponseMapper.INSTANCE::toDTO);
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
    @Transactional
    public void createRestaurant(RestaurantCreateRequest restaurantDTO) {
        User owner = userService.findById(restaurantDTO.getOwnerId());
        if (owner.getRestaurant() == null) {
            Restaurant restaurant = Restaurant.builder()
                    .name(restaurantDTO.getName())
                    .phone(restaurantDTO.getPhone())
                    .email(restaurantDTO.getEmail())
                    .address(restaurantDTO.getAddress())
                    .image(restaurantDTO.getImage())
                    .owner(owner)
                    .build();
            restaurantRepository.save(restaurant);

            for (int i = 0; i < 7; i++) {
                OperatingHour operatingHour = OperatingHour.builder()
                        .day(WEEKDAYS.get(i))
                        .isOpen(false)
                        .is24Hours(false)
                        .openingTime(LocalTime.of(7,0))
                        .closingTime(LocalTime.of(23,0))
                        .restaurant(restaurant)
                        .build();
                operatingHourService.createOperatingHour(operatingHour);
            }
        }
        else {
            throw new ElementExistException("The user has already owned a restaurant!");
        }

    }

    @Override
    @Transactional
    public void deleteRestaurant(Integer restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        restaurant.setDeleted(true);
        restaurantRepository.save(restaurant);
        operatingHourService.deleteOperatingHoursByRestaurantId(restaurantId);
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

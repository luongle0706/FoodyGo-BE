package com.foodygo.service.impl;

import com.foodygo.dto.OperatingHourDTO;
import com.foodygo.entity.OperatingHour;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OperatingHourMapper;
import com.foodygo.repository.OperatingHourRepository;
import com.foodygo.service.spec.OperatingHourService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperatingHourServiceImpl implements OperatingHourService {
    private final OperatingHourRepository repository;

    @Override
    public void createOperatingHour(OperatingHour operatingHour) {
        repository.save(operatingHour);
    }

    @Override
    public void updateOperatingHour(OperatingHourDTO operatingHourDTO) {

        OperatingHour exist = getOperatingHourById(operatingHourDTO.getId());
        exist.setOpen(operatingHourDTO.isOpen());
        exist.set24Hours(operatingHourDTO.isHours());
        exist.setOpeningTime(operatingHourDTO.getOpeningTime());
        exist.setClosingTime(operatingHourDTO.getClosingTime());
        repository.save(exist);
    }

    @Override
    @Transactional
    public void updateOperatingHoursByRestaurantId(List<OperatingHourDTO> requestList) {
        for (OperatingHourDTO request : requestList) {
            updateOperatingHour(request);
        }
    }

    @Override
    public void deleteOperatingHoursByRestaurantId(Integer restaurantId) {
        List<OperatingHour> list = getOperatingHoursByRestaurantId(restaurantId);
        if (list != null) {
            for (OperatingHour o : list) {
                o.setDeleted(true);
                repository.save(o);
            }
        }
    }

    @Override
    public OperatingHour getOperatingHourById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IdNotFoundException("Operating hour not found"));
    }

    @Override
    public List<OperatingHour> getOperatingHoursByRestaurantId(Integer restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    @Override
    public List<OperatingHourDTO> getOperatingHourDTOsByRestaurantId(Integer restaurantId) {
        List<OperatingHour> list = repository.findByRestaurantId(restaurantId);
        return list.stream().map(OperatingHourMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}

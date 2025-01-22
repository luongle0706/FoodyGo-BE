package com.foodygo.service;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.entity.AddonItem;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.AddonItemMapper;
import com.foodygo.repository.AddonItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonItemServiceImpl implements AddonItemService {

    private final AddonItemRepository addonItemRepository;

    @Override
    public List<AddonItem> getAddonItemsBySectionId(Integer sectionId) {
        return addonItemRepository.findBySectionIdAndDeletedFalse(sectionId);
    }

    @Override
    public Page<AddonItemDTO> getAddonItemsBySectionId(Integer sectionId, Pageable pageable) {
        return addonItemRepository.findBySectionIdAndDeletedFalse(sectionId, pageable)
                .map(AddonItemMapper.INSTANCE::toDTO);
    }

    @Override
    public AddonItem getAddonItemById(Integer id) {
        return addonItemRepository.findByIdAndDeletedFalse(id)
                .orElse(null);
    }

    @Override
    public AddonItemDTO getAddonItemDTOById(Integer id) {
        AddonItem addonItem = getAddonItemById(id);
        if (addonItem == null) {
            throw new ElementNotFoundException("Addon Item Not Found with id: " + id);
        }
        return AddonItemMapper.INSTANCE.toDTO(addonItem);
    }

    @Override
    @Transactional
    public AddonItem createAddonItem(AddonItemDTO.CreateRequest request) {
        AddonItem addonItem = AddonItem.builder()
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();
        return addonItemRepository.save(addonItem);
    }

    @Override
    @Transactional
    public AddonItemDTO createAddonItemDTO(AddonItemDTO.CreateRequest request) {
        return AddonItemMapper.INSTANCE.toDTO(createAddonItem(request));
    }

    @Override
    @Transactional
    public AddonItem updateAddonItem(AddonItemDTO.UpdateRequest request) {
        AddonItem addonItem = getAddonItemById(request.id());
        if (addonItem == null) {
            throw new ElementNotFoundException("Addon Item Not Found with id: " + request.id());
        }
        addonItem.setName(request.name());
        addonItem.setPrice(request.price());
        addonItem.setQuantity(request.quantity());
        return addonItemRepository.save(addonItem);
    }

    @Override
    @Transactional
    public AddonItemDTO updateAddonItemDTO(AddonItemDTO.UpdateRequest request) {
        return AddonItemMapper.INSTANCE.toDTO(updateAddonItem(request));
    }

    @Override
    @Transactional
    public void deleteAddonItem(Integer id) {
        AddonItem addonItem = getAddonItemById(id);
        if (addonItem == null) {
            throw new ElementNotFoundException("Addon Item Not Found with id: " + id);
        }
        addonItem.setDeleted(true);
        addonItemRepository.save(addonItem);
    }
}

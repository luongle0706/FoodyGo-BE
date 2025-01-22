package com.foodygo.service;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.entity.AddonItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddonItemService {

    List<AddonItem> getAddonItemsBySectionId(Integer sectionId);
    Page<AddonItemDTO> getAddonItemsBySectionId(Integer sectionId, Pageable pageable);

    AddonItem getAddonItemById(Integer id);
    AddonItemDTO getAddonItemDTOById(Integer id);

    AddonItem createAddonItem(AddonItemDTO.CreateRequest request);
    AddonItemDTO createAddonItemDTO(AddonItemDTO.CreateRequest request);

    AddonItem updateAddonItem(AddonItemDTO.UpdateRequest request);
    AddonItemDTO updateAddonItemDTO(AddonItemDTO.UpdateRequest request);

    void deleteAddonItem(Integer id);

}

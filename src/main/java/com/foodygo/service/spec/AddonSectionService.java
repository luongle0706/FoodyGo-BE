package com.foodygo.service.spec;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.entity.AddonSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddonSectionService {

    List<AddonSection> getAddonSectionsByProductId(Integer id);
    Page<AddonSectionDTO> getAddonSectionsByProductId(Integer id, Pageable pageable);

    AddonSection getAddonSectionById(Integer id);
    AddonSectionDTO getAddonSectionDTOById(Integer id);

    AddonSection createAddonSection(AddonSectionDTO.CreateRequest request);
    AddonSectionDTO createAddonSectionDTO(AddonSectionDTO.CreateRequest request);

    AddonSection updateAddonSection(AddonSectionDTO.UpdateRequest request);
    AddonSectionDTO updateAddonSectionDTO(AddonSectionDTO.UpdateRequest request);

    void deleteAddonSection(Integer id);

}

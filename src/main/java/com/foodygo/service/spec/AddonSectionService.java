package com.foodygo.service.spec;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.entity.AddonSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

public interface AddonSectionService {

    AddonSection getAddonSectionById(Integer id);
    AddonSectionDTO getAddonSectionDTOById(Integer id);

    AddonSection createAddonSection(AddonSectionDTO.CreateRequest request);
    AddonSectionDTO createAddonSectionDTO(AddonSectionDTO.CreateRequest request);

    AddonSection updateAddonSection(AddonSectionDTO.UpdateRequest request);
    AddonSectionDTO updateAddonSectionDTO(AddonSectionDTO.UpdateRequest request);

    void deleteAddonSection(Integer id);
    MappingJacksonValue getAddonSections(PagingRequest request);
}

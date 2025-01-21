package com.foodygo.service;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.entity.AddonSection;

import java.util.List;

public interface AddonSectionService {
    AddonSection getAddonSectionById(Integer addonSectionId);
    List<AddonSection> getAddonSectionsByProductId(Integer productId);
    List<AddonSection> getAllAddonSections();
    void createAddonSection(AddonSectionDTO addonSectionDTO);
    void updateAddonSection(AddonSectionDTO addonSectionDTO);
    void deleteAddonSection(Integer addonSectionId);
}

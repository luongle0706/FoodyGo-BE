package com.foodygo.service;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.entity.AddonSection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddonSectionServiceImpl implements AddonSectionService {
    @Override
    public AddonSection getAddonSectionById(Integer addonSectionId) {
        return null;
    }

    @Override
    public List<AddonSection> getAddonSectionsByProductId(Integer productId) {
        return null;
    }

    @Override
    public List<AddonSection> getAllAddonSections() {
        return null;
    }

    @Override
    public void createAddonSection(AddonSectionDTO addonSectionDTO) {

    }

    @Override
    public void updateAddonSection(AddonSectionDTO addonSectionDTO) {

    }

    @Override
    public void deleteAddonSection(Integer addonSectionId) {

    }
}

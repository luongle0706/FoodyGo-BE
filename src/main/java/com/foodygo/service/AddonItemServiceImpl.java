package com.foodygo.service;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.entity.AddonItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddonItemServiceImpl implements AddonItemService{
    @Override
    public AddonItem getAddonItemById(Integer addonItemId) {
        return null;
    }

    @Override
    public List<AddonItem> getAddonItemsByAddonSectionId(Integer addonSectionId) {
        return null;
    }

    @Override
    public List<AddonItem> getAllAddonItems() {
        return null;
    }

    @Override
    public void createAddonItem(AddonItemDTO addonItemDTO) {

    }

    @Override
    public void updateAddonItem(AddonItemDTO addonItemDTO) {

    }

    @Override
    public void deleteAddonItem(Integer addonItemId) {

    }
}

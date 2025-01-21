package com.foodygo.service;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.entity.AddonItem;

import java.util.List;

public interface AddonItemService {
    AddonItem getAddonItemById(Integer addonItemId);
    List<AddonItem> getAddonItemsByAddonSectionId(Integer addonSectionId);
    List<AddonItem> getAllAddonItems();
    void createAddonItem(AddonItemDTO addonItemDTO);
    void updateAddonItem(AddonItemDTO addonItemDTO);
    void deleteAddonItem(Integer addonItemId);
}

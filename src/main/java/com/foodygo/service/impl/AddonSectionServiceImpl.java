package com.foodygo.service.impl;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.entity.AddonSection;
import com.foodygo.entity.Product;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.AddonSectionMapper;
import com.foodygo.repository.AddonSectionRepository;
import com.foodygo.repository.ProductRepository;
import com.foodygo.service.spec.AddonSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonSectionServiceImpl implements AddonSectionService {

    private final AddonSectionRepository addonSectionRepository;
    private final ProductRepository productRepository;

    @Override
    public List<AddonSection> getAddonSectionsByProductId(Integer id) {
        return addonSectionRepository.findByProductIdAndDeletedFalse(id);
    }

    @Override
    public Page<AddonSectionDTO> getAddonSectionsByProductId(Integer id, Pageable pageable) {
        return addonSectionRepository.findByProductIdAndDeletedFalse(id, pageable)
                .map(AddonSectionMapper.INSTANCE::toDto);
    }

    @Override
    public AddonSection getAddonSectionById(Integer id) {
        return addonSectionRepository.findByIdAndDeletedFalse(id)
                .orElse(null);
    }

    @Override
    public AddonSectionDTO getAddonSectionDTOById(Integer id) {
        AddonSection addonSection = getAddonSectionById(id);
        if (addonSection == null) {
            throw new ElementNotFoundException("Addon section not found with id: " + id);
        }
        return AddonSectionMapper.INSTANCE.toDto(addonSection);
    }

    @Override
    @Transactional
    public AddonSection createAddonSection(AddonSectionDTO.CreateRequest request) {
        List<Product> listLinkProduct = new ArrayList<>();

        for (Integer productId : request.productId()) {
            Product product = productRepository.findByIdAndDeletedFalse(productId).orElseThrow(
                    () -> new ElementNotFoundException("Product not found with id: " + request.productId())
            );
            listLinkProduct.add(product);
        }
        AddonSection addonSection = AddonSection.builder()
                .name(request.name())
                .maxChoice(request.maxChoice())
                .required(request.required())
                .product(listLinkProduct)
                .build();
        return addonSectionRepository.save(addonSection);
    }

    @Override
    @Transactional
    public AddonSectionDTO createAddonSectionDTO(AddonSectionDTO.CreateRequest request) {
        return AddonSectionMapper.INSTANCE.toDto(createAddonSection(request));
    }

    @Override
    @Transactional
    public AddonSection updateAddonSection(AddonSectionDTO.UpdateRequest request) {
        AddonSection addonSection = getAddonSectionById(request.id());
        if (addonSection == null) {
            throw new ElementNotFoundException("Addon section not found with id: " + request.id());
        }
        addonSection.setName(request.name());
        addonSection.setMaxChoice(request.maxChoice());
        addonSection.setRequired(request.required());
        return addonSectionRepository.save(addonSection);
    }

    @Override
    @Transactional
    public AddonSectionDTO updateAddonSectionDTO(AddonSectionDTO.UpdateRequest request) {
        return AddonSectionMapper.INSTANCE.toDto(updateAddonSection(request));
    }

    @Override
    @Transactional
    public void deleteAddonSection(Integer id) {
        AddonSection addonSection = getAddonSectionById(id);
        if (addonSection == null) {
            throw new ElementNotFoundException("Addon section not found with id: " + id);
        }
        addonSection.setDeleted(true);
        addonSectionRepository.save(addonSection);
    }
}

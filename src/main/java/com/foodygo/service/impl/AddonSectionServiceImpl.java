package com.foodygo.service.impl;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.paging.AddonSectionPagingResponse;
import com.foodygo.dto.paging.OrderPagingResponse;
import com.foodygo.entity.AddonItem;
import com.foodygo.entity.AddonSection;
import com.foodygo.entity.Order;
import com.foodygo.entity.Product;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.mapper.AddonSectionMapper;
import com.foodygo.repository.AddonItemRepository;
import com.foodygo.repository.AddonSectionRepository;
import com.foodygo.repository.ProductRepository;
import com.foodygo.service.spec.AddonItemService;
import com.foodygo.service.spec.AddonSectionService;
import com.foodygo.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonSectionServiceImpl implements AddonSectionService {

    private final AddonSectionRepository addonSectionRepository;
    private final AddonItemRepository addonItemRepository;

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
        AddonSection addonSection = AddonSection.builder()
                .name(request.name())
                .maxChoice(request.maxChoice())
                .required(request.required())
                .build();
        AddonSection saved =  addonSectionRepository.save(addonSection);

        for (AddonSectionDTO.CreateRequest.AddonItemCreateRequest addonItem : request.addonItems()) {
            AddonItem addItem = AddonItem.builder()
                    .name(addonItem.name())
                    .price(addonItem.price())
                    .quantity(addonItem.quantity())
                    .section(addonSection)
                    .build();
            addonItemRepository.save(addItem);
        }
        return saved;
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

    @Override
    public MappingJacksonValue getAddonSections(PagingRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Specification<AddonSection> addonSectionSpecification = AddonSectionPagingResponse.filterByFields(request.getFilters());
        Page<AddonSection> page = addonSectionRepository.findAll(addonSectionSpecification, pageable);
        List<AddonSectionPagingResponse> mappedDTOs = page.getContent().stream().map(AddonSectionPagingResponse::fromEntity).toList();
        return PaginationUtil.getPagedMappingJacksonValue(request, page, mappedDTOs, "Get orders");
    }
}

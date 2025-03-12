package com.foodygo.controller;

import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.BuildingService;
import com.foodygo.service.spec.HubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {
    private final HubService hubService;
    private final BuildingService buildingService;

    @GetMapping("/hubs")
    public ResponseEntity<MappingJacksonValue> getAllHubs(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Map<String, String> filters
    ) {
        filters.remove("pageNo");
        filters.remove("pageSize");
        filters.remove("sortBy");
        filters.remove("filters");
        return ResponseEntity
                .status(OK)
                .body(
                        hubService.getHubsForSelection(PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .params(params)
                                .sortBy(sortBy)
                                .filters(filters)
                                .build())
                );
    }

    @GetMapping("/buildings")
    public ResponseEntity<MappingJacksonValue> getAllBuildings(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String params,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Map<String, String> filters
    ) {
        filters.remove("pageNo");
        filters.remove("pageSize");
        filters.remove("sortBy");
        filters.remove("filters");
        return ResponseEntity
                .status(OK)
                .body(
                        buildingService.getAllBuildings(PagingRequest.builder()
                                .pageNo(pageNo)
                                .pageSize(pageSize)
                                .params(params)
                                .sortBy(sortBy)
                                .filters(filters)
                                .build())
                );
    }
}

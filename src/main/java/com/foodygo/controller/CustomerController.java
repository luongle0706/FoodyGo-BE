package com.foodygo.controller;

import com.foodygo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sdw391/v1/customer")
public class CustomerController {

    private final CustomerService customerService;


}

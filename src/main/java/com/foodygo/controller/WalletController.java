package com.foodygo.controller;

import com.foodygo.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallet")
public class WalletController {

    private final WalletService walletService;

}

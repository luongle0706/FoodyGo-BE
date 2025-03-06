package com.foodygo.controller;

import com.foodygo.dto.request.WithdrawRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.service.spec.DepositService;
import com.foodygo.service.spec.TransactionService;
import com.foodygo.service.spec.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallet", description = "Operations related to wallet management")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final DepositService depositService;

    @Operation(summary = "Get Wallet by Customer ID", description = "Fetches the wallet balance and information for a customer based on the customer ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet found and balance retrieved"),
            @ApiResponse(responseCode = "400", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ObjectResponse> getWalletByCustomer(@Parameter(description = "ID of the customer") @PathVariable Integer customerId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Wallet found and balance retrieved")
                .data(walletService.getWalletByCustomerId(customerId))
                .build());
    }

    @Operation(summary = "Get Transactions for Wallet", description = "Fetches the transaction history for a specific wallet.")
    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<ObjectResponse> getTransactions(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Get transaction successfully")
                .data(transactionService.getTransactionsByWallet(walletId))
                .build());
    }

    @Operation(summary = "Get Transaction are payment type", description = "Fetches the details of a specific transaction based on the transaction ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction details retrieved"),
            @ApiResponse(responseCode = "400", description = "Transaction not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{walletId}/payments")
    public ResponseEntity<ObjectResponse> getTransactionByType(@Parameter(description = "ID of the transaction") @PathVariable Integer walletId) {
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Transaction of payments details retrieved")
                .data(transactionService.getTransactionByType(walletId))
                .build());
    }

    @Operation(summary = "Withdraw from Wallet", description = "Withdraws a specified amount from a wallet.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Withdrawal processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid withdrawal request"),
        @ApiResponse(responseCode = "400", description = "Wallet not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<ObjectResponse> withdraw(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId,
                                               @Parameter(description = "Withdrawal request details") @RequestBody WithdrawRequest request) {
        walletService.processWithdraw(walletId, request.getAmount());
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Withdrawal processed successfully")
                .build());
    }

}
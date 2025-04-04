package com.foodygo.controller;

import com.foodygo.dto.request.TopUpRequest;
import com.foodygo.dto.request.TransferRequest;
import com.foodygo.dto.request.WithdrawRequest;
import com.foodygo.dto.response.ObjectResponse;
import com.foodygo.dto.response.WalletsSummaryResponse;
import com.foodygo.service.spec.TransactionService;
import com.foodygo.service.spec.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallet", description = "Operations related to wallet management")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

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

    @Operation(summary = "Transfer funds", description = "Transfers funds from one wallet to another.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transfer processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{walletId}/transfer")
    public ResponseEntity<ObjectResponse> transfer(@Parameter(description = "ID of the sender's wallet") @PathVariable Integer walletId,
                                                   @Parameter(description = "Transfer request details") @RequestBody TransferRequest request) {
        walletService.processTransfer(walletId, request.getReceiver(), request.getAmount(), request.getNote());
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Transfer processed successfully")
                .build());
    }

   @Operation(summary = "Top up Wallet", description = "Tops up a wallet with a specified amount using a specified method.")
   @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Top up processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid top up request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{walletId}/topup")
    public ResponseEntity<ObjectResponse> topUpWallet(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId,
                                                      @Parameter(description = "Top up request details") @RequestBody TopUpRequest requestDto, HttpServletRequest request) {

    return ResponseEntity.ok(ObjectResponse.builder()
            .status("200")
            .message("Top up processed successfully")
            .data(walletService.processTopUp(walletId, requestDto.getAmount(), requestDto.getMethod(), request))
            .build());
    }

    @Operation(summary = "Get Wallets Summary", description = "Fetches the summary of all wallets.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallets summary retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<ObjectResponse> getWalletsSummary() {
        WalletsSummaryResponse summary = walletService.getWalletsSummary();
        return ResponseEntity.ok(ObjectResponse.builder()
                .status("200")
                .message("Wallets summary retrieved successfully")
                .data(summary)
                .build());
    }

}
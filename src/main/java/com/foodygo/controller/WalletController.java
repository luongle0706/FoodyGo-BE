package com.foodygo.controller;
import com.foodygo.dto.request.PaymentRequest;
import com.foodygo.dto.request.TransferRequest;
import com.foodygo.dto.response.TransactionHistoryResponse;
import com.foodygo.dto.response.WalletBalanceResponse;
import com.foodygo.entity.Deposit;
import com.foodygo.entity.Transaction;
import com.foodygo.entity.Wallet;
import com.foodygo.service.DepositService;
import com.foodygo.service.TransactionService;
import com.foodygo.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<WalletBalanceResponse> getWalletByCustomer(@Parameter(description = "ID of the customer") @PathVariable Integer customerId) {
        return ResponseEntity.ok(walletService.getWalletByCustomerId(customerId));
    }

    @Operation(summary = "Get Wallet by Restaurant ID", description = "Fetches the wallet balance and information for a restaurant based on the restaurant ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet found and balance retrieved"),
            @ApiResponse(responseCode = "400", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<WalletBalanceResponse> getWalletByRestaurant(@Parameter(description = "ID of the restaurant") @PathVariable Integer restaurantId) {
        return ResponseEntity.ok(walletService.getWalletByRestaurantId(restaurantId));
    }

    @Operation(summary = "Get Wallet Balance", description = "Fetches the current balance of a specific wallet.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Wallet deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Wallet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{walletId}/balance")
    public ResponseEntity<Double> getWalletBalance(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId) {
        return ResponseEntity.ok(walletService.getWalletBalance(walletId));
    }

    @Operation(summary = "Delete Wallet", description = "Deletes a wallet based on the wallet ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction history retrieved"),
            @ApiResponse(responseCode = "400", description = "Wallet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(@Parameter(description = "ID of the wallet to be deleted") @PathVariable Integer walletId) {
        walletService.deleteWallet(walletId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Transactions for Wallet", description = "Fetches the transaction history for a specific wallet.")
    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactions(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId) {
        return ResponseEntity.ok(transactionService.getTransactionsByWallet(walletId));
    }

    @Operation(summary = "Get Transaction by ID", description = "Fetches the details of a specific transaction based on the transaction ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction details retrieved"),
            @ApiResponse(responseCode = "400", description = "Transaction not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionHistoryResponse> getTransaction(@Parameter(description = "ID of the transaction") @PathVariable Integer transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @Operation(summary = "Make Payment from Wallet", description = "Processes a payment from a specific wallet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request"),
            @ApiResponse(responseCode = "400", description = "Wallet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{walletId}/pay")
    public ResponseEntity<TransactionHistoryResponse> pay(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId,
                                                          @Parameter(description = "Payment request details") @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(transactionService.processPayment(walletId, request.getAmount()));
    }

    @Operation(summary = "Process Refund to Wallet", description = "Processes a refund to a specific wallet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Refund processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refund request"),
            @ApiResponse(responseCode = "400", description = "Wallet not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{walletId}/refund")
    public ResponseEntity<TransactionHistoryResponse> refund(@Parameter(description = "ID of the wallet") @PathVariable Integer walletId,
                                                             @Parameter(description = "Refund request details") @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(transactionService.processRefund(walletId, request.getAmount()));
    }

    @Operation(summary = "Transfer Money between Wallets", description = "Transfers money from one wallet to another.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transfer completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
            @ApiResponse(responseCode = "400", description = "One or both wallets not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionHistoryResponse> transfer(@Parameter(description = "Details of the transfer request") @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transferMoney(request.getFromWalletId(), request.getToWalletId(), request.getAmount()));
    }

    @Operation(summary = "Request Deposit", description = "Creates a new deposit request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deposit request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid deposit request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/deposit")
    public ResponseEntity<Deposit> requestDeposit(@Parameter(description = "Details of the deposit request") @RequestBody Deposit deposit) {
        return ResponseEntity.ok(depositService.requestDeposit(deposit));
    }

    @Operation(summary = "Approve Deposit", description = "Approves a pending deposit request based on deposit ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deposit approved successfully"),
            @ApiResponse(responseCode = "400", description = "Deposit not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{depositId}/approve")
    public ResponseEntity<Void> approveDeposit(@Parameter(description = "ID of the deposit to approve") @PathVariable Integer depositId) {
        depositService.approveDeposit(depositId);
        return ResponseEntity.noContent().build();
    }
}
package com.foodygo.dto.response;

import com.foodygo.enums.TransactionType;
import com.foodygo.enums.WalletType;

import java.time.LocalDateTime;
import java.util.List;

public record WalletsSummaryResponse(
        Integer numOfWallet,
        Double amountOfSystem,
        Integer numOfWalletOfCustomer,
        Integer numOfWalletOfRestaurant,
        List<WalletResponse> wallets
) {
    public record WalletResponse(
            String id,
            Double balance,
            CustomerResponse customer,
            RestaurantResponse restaurant,
            WalletType walletType,
            List<TransactionResponse> transactions
    ) {
        public record CustomerResponse(
                Integer id,
                String name,
                String email
        ) {
        }

        public record RestaurantResponse(
                Integer id,
                String name,
                String address
        ) {
        }

        public record TransactionResponse(
                Integer id,
                String description,
                LocalDateTime time,
                Double amount,
                Double remaining,
                TransactionType type
        ) {
        }
    }
}
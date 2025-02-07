package com.foodygo.dto.request;

import lombok.Data;

@Data
public class TransferRequest {
    private Integer fromWalletId;
    private Integer toWalletId;
    private Double amount;
}

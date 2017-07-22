package com.ovargas.transactions.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class TransactionInfo {

    private double amount;

    private long timestamp;

}

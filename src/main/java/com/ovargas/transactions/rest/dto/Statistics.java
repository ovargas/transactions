package com.ovargas.transactions.rest.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Statistics {

    private double sum;

    private double avg;

    private double max;

    private double min;

    private long count;

}

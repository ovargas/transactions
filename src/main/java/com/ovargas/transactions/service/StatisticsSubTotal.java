package com.ovargas.transactions.service;

import lombok.Getter;


@Getter
class StatisticsSubTotal {

    private final long timestampIndex;

    private double min;

    private double max;

    private double total;

    private long count;

    public StatisticsSubTotal(long timestampIndex, double amount) {

        this.timestampIndex = timestampIndex;
        this.min = amount;
        this.max = amount;
        this.total = amount;
        this.count = 1;
    }

    public void update(double amount) {

        if (min > amount) {
            min = amount;
        }

        if (max < amount) {
            max = amount;
        }

        total += amount;
        count += 1;
    }
}

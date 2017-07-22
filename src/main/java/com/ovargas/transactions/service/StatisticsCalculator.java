package com.ovargas.transactions.service;

import com.ovargas.transactions.rest.dto.Statistics;

public class StatisticsCalculator {

    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double sum = 0D;
    private long count = 0L;

    public void calculate(StatisticsSubTotal subTotals) {

        min = min > subTotals.getMin() ? subTotals.getMin() : min;
        max = max < subTotals.getMax() ? subTotals.getMax() : max;
        sum += subTotals.getTotal();
        count += subTotals.getCount();
    }

    public Statistics buildStatistics() {

        if (this.count == 0) {
            return Statistics.builder().build();
        }

        return Statistics.builder()
                .sum(this.sum)
                .count(this.count)
                .avg(this.sum / this.count)
                .min(this.min)
                .max(this.max)
                .build();
    }
}

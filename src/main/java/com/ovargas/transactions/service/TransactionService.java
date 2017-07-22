package com.ovargas.transactions.service;

import com.ovargas.transactions.rest.dto.Statistics;
import com.ovargas.transactions.rest.dto.TransactionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final long MAX_TIME = 60000;

    private final long precision;
    private final long maxElements;

    private TransactionService(
            @Min(1)
            @Max(1000)
            @Value("${statistics.precision-milliseconds:10}") long precision) {
        this.precision = precision;
        maxElements = MAX_TIME / precision;

    }

    private final Object lockObject = new Object();


    private Map<Long, StatisticsSubTotal> bucket = new HashMap<>();

    public boolean addTransaction(TransactionInfo transactionInfo) {

        long now = Instant.now().toEpochMilli();

        if (transactionInfo.getTimestamp() < now - MAX_TIME) {
            return false;
        }

        long index = normalizeTimestamp(transactionInfo.getTimestamp());

        synchronized (lockObject) {

            StatisticsSubTotal item = bucket.getOrDefault(index, null);

            if (item != null) {
                item.update(transactionInfo.getAmount());
            } else {

                if (bucket.size() > maxElements) {
                    bucket.keySet().removeAll(
                            bucket.keySet().stream().filter(f -> f < now - MAX_TIME).collect(Collectors.toSet())
                    );
                }

                bucket.put(index, new StatisticsSubTotal(index, transactionInfo.getAmount()));

            }
        }

        return true;
    }


    public Statistics calculate() {

        long now = Instant.now().toEpochMilli();
        long from = normalizeTimestamp(now);

        synchronized (lockObject) {
            StatisticsCalculator calculator = new StatisticsCalculator();

            bucket.values().stream()
                    .filter(f -> f.getTimestampIndex() > from - MAX_TIME)
                    .forEach(calculator::calculate);

            return calculator.buildStatistics();
        }
    }

    private long normalizeTimestamp(long timestamp) {
        return timestamp - timestamp % precision + precision;
    }

}

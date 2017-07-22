package com.ovargas.transactions.rest;

import com.ovargas.transactions.rest.dto.Statistics;
import com.ovargas.transactions.rest.dto.TransactionInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    private final static String TRANSACTIONS_PATH = TransactionController.BASE_PATH + "transactions";
    private final static String STATISTICS_PATH = TransactionController.BASE_PATH + "statistics";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddTransaction() {

        long now = Instant.now().toEpochMilli() - 4000;

        TransactionInfo info = new TransactionInfo();

        info.setAmount(10.5);
        info.setTimestamp(now);

        ResponseEntity<String> response = restTemplate.exchange(
                TRANSACTIONS_PATH,
                HttpMethod.POST,
                new HttpEntity<>(info), String.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    public void testOlderTransaction() {

        TransactionInfo info = new TransactionInfo();

        info.setAmount(10.5);
        info.setTimestamp(1000000);

        ResponseEntity<String> response = restTemplate.exchange(
                TRANSACTIONS_PATH,
                HttpMethod.POST,
                new HttpEntity<>(info), String.class);

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    public void testCalculateStatistics() {

        long now = Instant.now().toEpochMilli();

        int numberOfTransactions = 10000;

        IntStream.range(0, numberOfTransactions).parallel().forEach(i -> {

            TransactionInfo info = new TransactionInfo();
            info.setAmount(i % 100 + 1);
            info.setTimestamp(now - numberOfTransactions - i);
            ResponseEntity<String> response = restTemplate.exchange(TRANSACTIONS_PATH, HttpMethod.POST,
                    new HttpEntity<>(info), String.class);
            Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        });

        Statistics actual = restTemplate.getForObject(STATISTICS_PATH, Statistics.class);

        Assert.assertEquals("Expected count", 10000L, actual.getCount());
        Assert.assertEquals("Expected sum", 505000D, actual.getSum(), 0D);
        Assert.assertEquals("Expected avg", 50.5D, actual.getAvg(), 0D);
        Assert.assertEquals("Expected min", 1D, actual.getMin(), 0D);
        Assert.assertEquals("Expected max", 100D, actual.getMax(), 0D);

    }

    @Test
    public void testEmptyStatistics() {
        Statistics actual = restTemplate.getForObject(STATISTICS_PATH, Statistics.class);

        Assert.assertEquals("Expected count", 0L, actual.getCount());
        Assert.assertEquals("Expected sum", 0D, actual.getSum(), 0D);
        Assert.assertEquals("Expected avg", 0D, actual.getAvg(), 0D);
        Assert.assertEquals("Expected min", 0D, actual.getMin(), 0D);
        Assert.assertEquals("Expected max", 0D, actual.getMax(), 0D);
    }
}

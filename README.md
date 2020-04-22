# Statistics calculation

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ovargas_transactions&metric=alert_status)](https://sonarcloud.io/dashboard?id=ovargas_transactions)

## API

### Start the service

``` bash
    mvn spring-boot:run
```

### Execute tests

``` bash
    mvn clean -U verify
```

### Install

``` bash
    mvn clean -U install
```

## Endpoints

#### Add transaction

Every Time a new transaction happened, this endpoint will be called.

##### Request

`POST /transactions`

``` javascript
{
    "amount": 12.3,
    "timestamp": 1478192204000
}
```

| Field | Description |
| ----- | ----------- |
| `amount` | Transaction amount |
| `timestamp` | Transaction time in epoch in millis in UTC time zone (this is not current timestamp) |


##### Response

| Http Status | Description |
| ----------- | ----------- |
| 201 | Transaction successfully added |
| 204 | Transaction is older than 60 seconds |

#### Get statistics

Get the statistic based on the transactions which happened in the last 60 seconds.

##### Request

`GET /statistics`

##### Response

``` javascript
{
    "sum": 1000,
    "avg": 100,
    "max": 200,
    "min": 50,
    "count": 10
}
```

| Field | Description |
| ----- | ----------- |
| `sum` | Is a double specifying the total sum of transaction value in the last 60 seconds |
| `avg` | Is a double specifying the average amount of transaction value in the last 60 seconds |
| `max` | Is a double specifying single highest transaction value in the last 60 seconds |
| `min` | Is a double specifying single lowest transaction value in the last 60 seconds |
| `count` | Is a long specifying the total number of transactions happened in the last 60 seconds |


## Configuration

``` yml

# Configure port to use

server:
  port: 9000

# Precision in millisecods to calculate statitics.
# Min value 1
# Max value 1000

statistics.precision-milliseconds: 10

```

## Docker support

### Create Image

``` bash
mvn clean package docker:build
```

### Execute image

``` bash
docker-compose up
```



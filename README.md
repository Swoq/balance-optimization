# Balance Optimisation Repository

This is a Kotlin Spring Boot application designed to investigate impact of different parameters and approaches on relation database performance. 
It provides a REST endpoint to update user balances in a PostgreSQL database. The application is designed to handle large amounts of data and high-frequency update operations.

## Metrics

### Spring JPA + Hibernate + HikariCP

Basically the [UserControllerPerformanceTest.kt](balance-bulk%2Fsrc%2Ftest%2Fkotlin%2Fcom%2Fswoqe%2Fbalance_bulk%2FUserControllerPerformanceTest.kt) generates 100k records to insert them 
and then query 10k Ids to alter the balance and execute the update. Metrics below are empirical and cannot be fully trusted, but they give a general idea of the performance and impact of
spring.jpa.properties.hibernate.jdbc.batch_size property.

| Hibernate Batch Size | Time to Update 10k (Millis) | Time to Insert 100k (Millis) |
|----------------------|-----------------------------|------------------------------|
| 10                   | 3927                        | 3074                         |
| 100                  | 2923                        | 2189                         |
| 500                  | 3012                        | 1840                         |
| 1000                 | 2669                        | 1618                         |
| 2000                 | 2641                        | 1851                         |
| 5000                 | 2739                        | 1525                         |

### Spring Reactive Web + R2DBC

It was going to show better performance than the conventional approach, but it was not the case. I think that R2DBC is a bottleneck in communication with PostgresSQL. Maybe using of Reactive Hibernate would 
make a difference, but the technology is not mature enough. Didn't have enough time to test it.

100k insert in 58393 ms
10k update in 5317 ms

## Possible Performance Enhancements

- Implement a caching layer using Redis or a similar in-memory data store to reduce database load for frequently accessed data.

- Investigate impact of the batch size in lab conditions for database updates. Add batch processing logic to minimize database write operations.

- Sharding of the database on user id. It would allow us distribute the load across multiple database instances.


## Requirements

- JDK 21 or higher
- Gradle 6.0 or higher
- PostgreSQL database

## Environment Variables
- POSTGRES_HOST. Default: jdbc:postgresql://localhost:5432/balance_db}
- POSTGRES_USERNAME. Default: postgres}
- POSTGRES_PASSWORD. Default: postgres}



